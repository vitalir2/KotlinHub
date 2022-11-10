package io.vitalir.server.kgit

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.vitalir.server.kgit.client.KtorHttpClient
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
            host = "0.0.0.0",
            port = 8081,
            servletPath = "/git/*",
        ),
        rootDirAbsolute = GitConstants.REPOSITORIES_PATH,
    )
}

private fun createApplicationGraph(serverConfig: ServerConfig): ApplicationGraph {
    val ktorHttpClient = HttpClient(CIO) {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
            }
        }
        install(ContentNegotiation) {
            json()
        }
    }
    val httpClient = KtorHttpClient(ktorHttpClient)
    val gitAuthManager = GitAuthManagerImpl(httpClient)
    val repositoryResolver = KGitRepositoryResolver(serverConfig)
    return ApplicationGraph(
        serverConfig = serverConfig,
        httpClient = httpClient,
        gitGraph = ApplicationGraph.Git(
            gitAuthManager = gitAuthManager,
            repositoryResolver = repositoryResolver,
            receivePackFilters = listOf(
                KGitAuthFilter(gitAuthManager),
            ),
            uploadPackFilters = listOf(
                KGitAuthFilter(gitAuthManager),
            ),
        )
    )
}

private fun ServerConnector.withConfig(networkConfig: ServerConfig.Network): ServerConnector = apply {
    host = networkConfig.host
    port = networkConfig.port
}

private fun ServletContextHandler.setupDefaultGitServlet(
    serverConfig: ServerConfig,
    gitGraph: ApplicationGraph.Git,
): ServletContextHandler =
    apply {
        contextPath = "/"
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
