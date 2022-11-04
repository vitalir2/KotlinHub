package io.vitalir.server.kgit

data class ServerConfig(
    val network: Network,
    val rootDirAbsolute: String,
) {

    data class Network(
        val host: String,
        val port: Int,
        val servletPath: String,
    )
}
