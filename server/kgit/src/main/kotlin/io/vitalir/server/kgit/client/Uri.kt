package io.vitalir.server.kgit.client


typealias QueryParams = Map<String, String>

data class Uri(
    val protocol: Protocol,
    val host: String,
    val port: Int,
    val path: String,
    val queryParams: QueryParams = emptyMap(),
) {

    constructor(
        protocol: Protocol,
        host: String,
        port: Int,
        pathSegments: List<String>,
        queryParams: QueryParams = emptyMap(),
    ) : this(
        protocol = protocol,
        host = host,
        port = port,
        path = pathSegments.joinToString(separator = "/"),
        queryParams = queryParams,
    )

    enum class Protocol(val uriValue: String) {
        HTTP("http"),
        HTTPS("https"),
    }
}
