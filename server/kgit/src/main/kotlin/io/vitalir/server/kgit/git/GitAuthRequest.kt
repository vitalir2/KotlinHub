package io.vitalir.server.kgit.git

internal data class GitAuthRequest(
    val repositoryName: String,
    val username: String?,
)
