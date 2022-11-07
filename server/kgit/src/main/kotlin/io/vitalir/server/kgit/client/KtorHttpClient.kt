package io.vitalir.server.kgit.client

import ch.qos.logback.classic.Logger
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.slf4j.LoggerFactory

internal class KtorHttpClient(
    private val httpClient: HttpClient,
) : KGitHttpClient {

    // TODO add interceptors
    private val logger = LoggerFactory.getLogger(Logger::class.java)

    override suspend fun post(
        uri: Uri,
        body: Any,
    ): Response {
        val response = httpClient.post {
            setUrlFromUri(uri)
            setBody(body)
            contentType(ContentType.Application.Json)
            logger.debug("Post request: uri=$uri,body=$body,builtURL=${url.buildString()}")
        }
        val httpCode = response.status
        return Response(code = Response.HttpCode.fromNumber(httpCode.value))
    }

    companion object {
        private fun HttpRequestBuilder.setUrlFromUri(uri: Uri) {
            url {
                protocol = uri.protocol.toKtorModel()
                host = uri.host
                port = uri.port
                path(uri.path)
                for ((key, value) in uri.queryParams) {
                    parameter(key, value)
                }
            }
        }

        private fun Uri.Protocol.toKtorModel(): URLProtocol = when (this) {
            Uri.Protocol.HTTP -> URLProtocol.HTTP
            Uri.Protocol.HTTPS -> URLProtocol.HTTPS
        }
    }
}
