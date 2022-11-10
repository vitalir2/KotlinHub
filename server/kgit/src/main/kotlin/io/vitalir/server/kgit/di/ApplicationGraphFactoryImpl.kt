package io.vitalir.server.kgit.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.vitalir.server.kgit.KGitRepositoryResolver
import io.vitalir.server.kgit.ServerConfig
import io.vitalir.server.kgit.client.KtorHttpClient
import io.vitalir.server.kgit.git.GitAuthManagerImpl
import io.vitalir.server.kgit.git.KGitAuthFilter

internal class ApplicationGraphFactoryImpl(
    private val serverConfig: ServerConfig,
) : ApplicationGraphFactory {

    override fun create(): ApplicationGraph {
        val ktorHttpClient = HttpClient(CIO) {
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTP
                }
            }
            install(ContentNegotiation) {
                json()
            }
        }
        val httpClient = KtorHttpClient(ktorHttpClient)
        val gitAuthManager = GitAuthManagerImpl(httpClient)
        val repositoryResolver = KGitRepositoryResolver(serverConfig)
        return ApplicationGraph(
            serverConfig = serverConfig,
            httpClient = httpClient,
            gitGraph = ApplicationGraph.Git(
                gitAuthManager = gitAuthManager,
                repositoryResolver = repositoryResolver,
                receivePackFilters = listOf(
                    KGitAuthFilter(gitAuthManager),
                ),
                uploadPackFilters = listOf(
                    KGitAuthFilter(gitAuthManager),
                ),
            )
        )
    }
}
