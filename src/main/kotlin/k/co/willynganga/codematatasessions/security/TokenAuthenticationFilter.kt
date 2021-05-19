package k.co.willynganga.codematatasessions.security

import k.co.willynganga.codematatasessions.service.OAuthUserService
import org.slf4j.LoggerFactory
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val oAuthUserService: OAuthUserService
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(TokenAuthenticationFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        try {
            val jwt = getJwtFromRequest(request)

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt!!)) {
                logger.debug("user id ${tokenProvider.getUserIdFromToken(jwt)}")
                val userId = tokenProvider.getUserIdFromToken(jwt)

                val userExists = oAuthUserService.existsBySub(userId)
                if (!userExists) {
                    logger.debug(request.attributeNames)
                }
            }
        } catch (t: Throwable) {
            logger.error("Could not set user authentication in security context")
        }
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}