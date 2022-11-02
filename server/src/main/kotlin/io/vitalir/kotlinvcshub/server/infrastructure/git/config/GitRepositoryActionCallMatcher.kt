package io.vitalir.kotlinvcshub.server.infrastructure.git.config

internal fun interface GitRepositoryActionCallMatcher {

    fun matches(uri: String): Boolean
}
