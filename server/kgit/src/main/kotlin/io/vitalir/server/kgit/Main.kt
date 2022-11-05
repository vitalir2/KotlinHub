package io.vitalir.server.kgit

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

    val connector = ServerConnector(this).withConfig(serverConfig.network)
    addConnector(connector)

    val servletHandler = ServletContextHandler().setupDefaultGitServlet(serverConfig)
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
        rootDirAbsolute = "/tmp/srv/repos", // TODO
    )
}

private fun ServerConnector.withConfig(networkConfig: ServerConfig.Network): ServerConnector = apply {
    host = networkConfig.host
    port = networkConfig.port
}

private fun ServletContextHandler.setupDefaultGitServlet(serverConfig: ServerConfig): ServletContextHandler = apply {
    contextPath = "/" // TODO think
    val servlet = GitServlet().apply {
        setRepositoryResolver(KGitRepositoryResolver(serverConfig))
    }
    val servletHolder = ServletHolder(servlet)
    addServlet(servletHolder, "/*/*.git")
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
