package io.vitalir.kotlinvcshub.server.infrastructure.git.internal

import io.vitalir.kotlinvcshub.server.infrastructure.git.config.GitUriParser

internal data class GitActionRequest(
    val repositoryRootPath: String,
    val parsedGitUri: GitUriParser.Result,
    val queryParams: Map<String, String>,
)
