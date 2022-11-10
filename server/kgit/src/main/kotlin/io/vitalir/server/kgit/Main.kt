package io.vitalir.server.kgit

import ch.qos.logback.classic.Logger
import io.vitalir.server.kgit.di.ApplicationGraph
import io.vitalir.server.kgit.di.ApplicationGraphFactoryImpl
import javax.servlet.http.HttpServletRequest
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.eclipse.jgit.http.server.GitServlet
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.transport.resolver.FileResolver
import org.eclipse.jgit.transport.resolver.RepositoryResolver
import org.slf4j.LoggerFactory
import kotlin.io.path.Path


fun main() = runServer {
    val logger = LoggerFactory.getLogger(Logger::class.java)
    val serverConfig = readServerConfig()
    logger.debug("Read server config=$serverConfig")
    val appGraphFactory = ApplicationGraphFactoryImpl(serverConfig)
    val appGraph = appGraphFactory.create()

    val connector = ServerConnector(this).withConfig(serverConfig.network)
    addConnector(connector)

    val servletHandler = ServletContextHandler().setupDefaultGitServlet(serverConfig, appGraph.gitGraph)
    handler = servletHandler
}

private fun runServer(block: Server.() -> Unit) {
    Server().apply(block).start()
}

private fun readServerConfig(): ServerConfig {
    val environment = System.getenv()
    return ServerConfig(
        network = ServerConfig.Network(
            host = environment["KGIT_HOST"] ?: "0.0.0.0",
            port = environment["KGIT_PORT"]?.toInt() ?: 8081,
            servletPath = environment["KGIT_BASE_PATH"] ?: "/git/*",
        ),
        rootDirAbsolute = environment["BASE_REPOSITORIES_PATH"].orEmpty(),
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
