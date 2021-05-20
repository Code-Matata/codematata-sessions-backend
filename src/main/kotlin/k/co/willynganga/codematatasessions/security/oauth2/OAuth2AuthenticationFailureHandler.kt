package k.co.willynganga.codematatasessions.security.oauth2

import k.co.willynganga.codematatasessions.other.Constants.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import k.co.willynganga.codematatasessions.util.CookieUtils
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationFailureHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository
) : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        exception: AuthenticationException?
    ) {
        var targetUrl =
            CookieUtils.getCookie(request!!, REDIRECT_URI_PARAM_COOKIE_NAME)?.value ?: "/"

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
            .query("error ${exception?.localizedMessage}")
            .build().toUriString()

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizedRequestCookies(request, response!!)

        redirectStrategy.sendRedirect(request, response, targetUrl)
    }
}