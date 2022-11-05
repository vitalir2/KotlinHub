package io.vitalir.server.kgit.git

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

internal class KGitAuthFilter(
    private val gitAuthManager: GitAuthManager,
) : Filter {

    @WorkerThread
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) = runBlocking {
        request as? HttpServletRequest ?: run {
            // Skip filtering because it's not http one
            chain?.doFilter(request, response)
            return@runBlocking
        }

        response as? HttpServletResponse ?: run {
            chain?.doFilter(request, response)
            return@runBlocking
        }

        var repositoryName: String? = request.pathInfo
        while (!repositoryName.isNullOrEmpty() && repositoryName[0] == '/'){
            repositoryName = repositoryName.substring(1)
        }
        if (repositoryName.isNullOrEmpty()) {
            GitSmartHttpTools.sendError(request, response, HttpServletResponse.SC_NOT_FOUND)
            return@runBlocking
        }

        val username: String? = request.remoteUser

        val hasAccess = gitAuthManager.hasAccess(
            repositoryName = repositoryName,
            username = username,
        )
        if (hasAccess) {
            chain?.doFilter(request, response)
        } else {
            throw ServiceNotAuthorizedException()
        }
    }
}
