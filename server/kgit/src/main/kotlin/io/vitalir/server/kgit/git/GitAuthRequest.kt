package io.vitalir.server.kgit.git

import kotlinx.serialization.Serializable

@Serializable
internal data class GitAuthRequest(
    val repositoryName: String,
    val username: String?,
    val service: GitService,
)
