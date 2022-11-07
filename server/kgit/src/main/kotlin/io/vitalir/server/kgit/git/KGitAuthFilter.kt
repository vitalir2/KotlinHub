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
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException
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

        val service = getServiceOrNullFromUrl(request.requestURL?.toString()) ?: run {
            logDebug { "Skip: Git service name is not found in URL=${request.requestURL}" }
            chain?.doFilter(request, response)
            return@runBlocking
        }

        val repositoryPath = parseRepositoryPathFromRequestPathInfo(request.pathInfo)
        if (repositoryPath.isNullOrEmpty()) {
            GitSmartHttpTools.sendError(request, response, HttpServletResponse.SC_NOT_FOUND)
            return@runBlocking
        }

        val (username, repositoryName) = repositoryPath.split("/")

        logDebug { "Request to check access for repository: repository=$repositoryPath, username=$username, service=.." }
        val hasAccess = gitAuthManager.hasAccess(
            repositoryName = repositoryName,
            username = username,
            service = service,
        )

        if (hasAccess) {
            chain?.doFilter(request, response)
        } else {
            throw ServiceNotAuthorizedException()
        }
    }

    private fun getServiceOrNullFromUrl(url: String?): GitService? {
        val serviceString = url?.takeLastWhile { it != '/' }
        return serviceString?.let(GitService::fromString)
    }

    private fun parseRepositoryPathFromRequestPathInfo(pathInfo: String?): String? {
        return pathInfo
            ?.takeWhile { char -> char == '/' }
    }

    private inline fun logDebug(message: () -> String) {
        logger.debug("KGitAuthFilter: ${message()}")
    }
}
