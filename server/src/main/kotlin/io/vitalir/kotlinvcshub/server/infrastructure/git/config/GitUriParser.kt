package io.vitalir.kotlinvcshub.server.infrastructure.git.config

internal fun interface GitUriParser {

    fun parse(uri: String): Result?

    data class Result(
        val userLogin: String,
        val repositoryName: String,
        val queryParams: QueryParams,
    )
}

internal typealias QueryParams = Map<String, String>
