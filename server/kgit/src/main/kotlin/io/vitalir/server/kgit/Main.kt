package io.vitalir.server.kgit

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jgit.http.server.GitServlet

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
    addServlet(GitServlet::class.java, serverConfig.network.servletPath).apply {
        setInitParameter("base-path", serverConfig.rootDirAbsolute)

        // Enable exporting all subdirs of the root
        setInitParameter("export-all", "1")
    }
}
