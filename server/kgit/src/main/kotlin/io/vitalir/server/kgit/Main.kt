package io.vitalir.server.kgit

import io.vitalir.server.kgit.client.MockHttpClient
import io.vitalir.server.kgit.client.Response
import io.vitalir.server.kgit.di.ApplicationGraph
import io.vitalir.server.kgit.git.GitAuthManagerImpl
import io.vitalir.server.kgit.git.GitConstants
import io.vitalir.server.kgit.git.KGitAuthFilter
import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jgit.http.server.GitServlet
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.resolver.FileResolver
import org.eclipse.jgit.transport.resolver.RepositoryResolver
import kotlin.io.path.Path


// TODO:
//  1. Create filter for auth (/git/http/auth)
//  2. Refactor it
//  3. You're cool
fun main() = runServer {
    val serverConfig = readServerConfig()
    val appGraph = createApplicationGraph(serverConfig)

    val connector = ServerConnector(this).withConfig(serverConfig.network)
    addConnector(connector)

    val servletHandler = ServletContextHandler().setupDefaultGitServlet(serverConfig, appGraph.gitGraph)
    handler = servletHandler
}

private fun runServer(block: Server.() -> Unit) {
    Server().apply(block).start()
}

// TODO read for real
private fun readServerConfig(): ServerConfig {
    return ServerConfig(
        network = ServerConfig.Network(
            host = "localhost",
            port = 8081,
            servletPath = "/git/*", // TODO
        ),
        rootDirAbsolute = GitConstants.REPOSITORIES_PATH,
    )
}

private fun createApplicationGraph(serverConfig: ServerConfig): ApplicationGraph {
    val httpClient = MockHttpClient(
        onGet = { _, _ -> Response(code = Response.HttpCode.OK) } // Used only for git auth manager
    )
    val gitAuthManager = GitAuthManagerImpl(httpClient)
    val repositoryResolver = KGitRepositoryResolver(serverConfig)
    return ApplicationGraph(
        serverConfig = serverConfig,
        httpClient = httpClient,
        gitGraph = ApplicationGraph.Git(
            gitAuthManager = gitAuthManager,
            repositoryResolver = repositoryResolver,
            receivePackFilters = listOf(
                KGitAuthFilter(gitAuthManager)
            ),
            uploadPackFilters = emptyList(),
        )
    )
}

private fun ServerConnector.withConfig(networkConfig: ServerConfig.Network): ServerConnector = apply {
    host = null // TODO refactor
    port = networkConfig.port
}

private fun ServletContextHandler.setupDefaultGitServlet(
    serverConfig: ServerConfig,
    gitGraph: ApplicationGraph.Git,
): ServletContextHandler =
    apply {
        contextPath = "/" // TODO think
        val servlet = GitServlet().apply {
            setRepositoryResolver(gitGraph.repositoryResolver)
            gitGraph.receivePackFilters.forEach(::addReceivePackFilter)
            gitGraph.uploadPackFilters.forEach(::addUploadPackFilter)
        }
        val servletHolder = ServletHolder(servlet)
        addServlet(servletHolder, serverConfig.network.servletPath)
    }

class KGitRepositoryResolver(
    serverConfig: ServerConfig,
) : RepositoryResolver<HttpServletRequest> {

    private val delegate: FileResolver<HttpServletRequest> = FileResolver()

    init {
        with(delegate) {
            exportDirectory(Path(serverConfig.rootDirAbsolute).toFile())
            isExportAll = true
        }
    }

    override fun open(req: HttpServletRequest?, name: String?): Repository {
        return delegate.open(req, name)
    }
}
