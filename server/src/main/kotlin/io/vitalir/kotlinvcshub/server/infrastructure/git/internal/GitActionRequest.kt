package io.vitalir.kotlinvcshub.server.infrastructure.git.internal

data class GitActionRequest(
    val repositoryRootPath: String,
    val networkPath: String,
    val queryParams: Map<String, String>,
)
