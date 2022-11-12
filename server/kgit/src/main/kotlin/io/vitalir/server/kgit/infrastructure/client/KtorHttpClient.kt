package io.vitalir.server.kgit.infrastructure.client

import ch.qos.logback.classic.Logger
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.vitalir.kotlinhub.shared.common.network.Scheme
import io.vitalir.kotlinhub.shared.common.network.Url
import org.slf4j.LoggerFactory

internal class KtorHttpClient(
    private val httpClient: HttpClient,
) : KGitHttpClient {

    // TODO add interceptors
    private val logger = LoggerFactory.getLogger(Logger::class.java)

    override suspend fun post(
        url: Url,
        body: Any,
    ): Response {
        val response = httpClient.post {
            setKtorUrl(url)
            setBody(body)
            contentType(ContentType.Application.Json)
            logger.debug("Post request: uri=$url,body=$body,builtURL=${this.url.buildString()}")
        }
        val httpCode = response.status
        return Response(code = Response.HttpCode.fromNumber(httpCode.value))
    }

    companion object {
        private fun HttpRequestBuilder.setKtorUrl(url: Url) {
            url {
                protocol = url.scheme.toKtorModel()
                host = url.host
                url.port?.let { notNullPort -> port = notNullPort }
                path(url.path.toString())
                for ((key, value) in url.queryParams) {
                    parameter(key, value)
                }
            }
        }

        private fun Scheme.toKtorModel(): URLProtocol = when (this) {
            Scheme.HTTP -> URLProtocol.HTTP
            Scheme.HTTPS -> URLProtocol.HTTPS
        }
    }
}
