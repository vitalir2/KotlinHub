package io.vitalir.kotlinvcshub.server.infrastructure.git.internal

// TODO
internal interface GitApi {
    suspend fun push(repositoryPath: String)

    suspend fun pull(repositoryPath: String)
}
