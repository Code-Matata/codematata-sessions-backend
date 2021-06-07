package k.co.willynganga.codematatasessions.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint::class.java)
    }

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        response?.sendError(
            HttpServletResponse.SC_UNAUTHORIZED,
            exception?.localizedMessage
        )
    }
}