package io.vitalir.server.kgit.git

import ch.qos.logback.classic.Logger
import io.vitalir.server.kgit.client.WorkerThread
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlinx.coroutines.runBlocking
import org.eclipse.jgit.http.server.GitSmartHttpTools
import org.slf4j.LoggerFactory

internal class KGitAuthFilter(
    private val gitAuthManager: GitAuthManager,
) : Filter {

    private val logger = LoggerFactory.getLogger(Logger::class.java)

    @WorkerThread
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) = runBlocking {
        logDebug { "doFilter" }

        request as? HttpServletRequest ?: run {
            // Skip filtering because it's not http one
            logDebug { "Skip: Request is not HTTP but $request" }
            chain?.doFilter(request, response)
            return@runBlocking
        }

        response as? HttpServletResponse ?: run {
            logDebug { "Skip: Response is not HTTP but $response" }
            chain?.doFilter(request, response)
            return@runBlocking
        }

        val repositoryPath = parseRepositoryPathFromRequestPathInfo(request.pathInfo)
        if (repositoryPath.isNullOrEmpty()) {
            GitSmartHttpTools.sendError(request, response, HttpServletResponse.SC_NOT_FOUND)
            return@runBlocking
        }

        val (username, repositoryName) = repositoryPath.split("/")
        logDebug { "Headers: ${request.headerNames.toList().joinToString(separator = ";")}" }
        val baseAuthCredentials = request.getHeader("Authorization")
            ?.let(AuthorizationHeader.BASIC::valueFromHeader)

        logDebug { """
                Request to check access for repository: repository=$repositoryName, username=$username, credentials=$baseAuthCredentials
                URL=${request.requestURL}, pathInfo=${request.pathInfo}
            """.trimIndent()
        }
        val hasAccess = gitAuthManager.hasAccess(
            repositoryName = repositoryName,
            username = username,
            credentials = baseAuthCredentials,
        )

        if (hasAccess) {
            chain?.doFilter(request, response)
        } else {
            requestCredentialsInput(response)
        }
    }

    private fun parseRepositoryPathFromRequestPathInfo(pathInfo: String?): String? {
        return pathInfo
            ?.removeWhile { char -> char == '/' }
    }

    private fun requestCredentialsInput(response: HttpServletResponse) {
        response.apply {
            setHeader("WWW-Authenticate", AuthorizationHeader.BASIC.name)
        }.sendError(401)
    }

    private inline fun logDebug(message: () -> String) {
        logger.debug("KGitAuthFilter: ${message()}")
    }

    companion object {
        private inline fun String.removeWhile(predicate: (Char) -> Boolean): String {
            val newString = StringBuilder(this)
            for (char in this) {
                if (!predicate(char)) {
                    break
                }
                newString.deleteCharAt(0)
            }
            return newString.toString()
        }
    }
}
