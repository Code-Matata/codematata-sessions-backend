package k.co.willynganga.codematatasessions.security.oauth2

import k.co.willynganga.codematatasessions.config.AppProperties
import k.co.willynganga.codematatasessions.model.OAuthUser
import k.co.willynganga.codematatasessions.other.Constants.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import k.co.willynganga.codematatasessions.other.ROLE
import k.co.willynganga.codematatasessions.security.CustomOAuth2User
import k.co.willynganga.codematatasessions.security.TokenProvider
import k.co.willynganga.codematatasessions.service.OAuthUserService
import k.co.willynganga.codematatasessions.util.CookieUtils
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler(
    private val tokenProvider: TokenProvider,
    private val appProperties: AppProperties,
    private val oAuthUserService: OAuthUserService
) : SimpleUrlAuthenticationSuccessHandler() {

    private val logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler::class.java)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        val targetUrl = determineTargetUrl(request!!, response!!, authentication!!)

        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }

        clearAuthenticationAttributes(request)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ): String {
        val redirectUri =
            CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.value

        if (!isAuthorizedRedirectUri(redirectUri)) {
            throw RuntimeException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication")
        }

        val userPrincipal = authentication.principal as CustomOAuth2User

        persistUser(userPrincipal)

        val targetUrl = redirectUri ?: defaultTargetUrl

        val token = tokenProvider.createToken(authentication)

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", token)
            .build().toUriString()
    }

    private fun persistUser(userPrincipal: CustomOAuth2User) {
        val userExists = oAuthUserService.existsBySub(userPrincipal.name)

        if (userExists) return

        val user = OAuthUser(
            userPrincipal.attributes["sub"].toString(),
            userPrincipal.attributes["name"].toString(),
            userPrincipal.attributes["given_name"].toString(),
            userPrincipal.attributes["family_name"].toString(),
            userPrincipal.attributes["picture"].toString(),
            userPrincipal.attributes["email"].toString(),
            ROLE.STUDENT
        )
        oAuthUserService.addUser(user)
    }

    private fun isAuthorizedRedirectUri(redirectUri: String?): Boolean {
        val clientRedirectUri = URI.create(redirectUri!!)

        return appProperties.getOAuth2().authorizedRedirectUris
            .any { authorizedUri ->
                val uri = URI.create(authorizedUri)

                if (uri.host == clientRedirectUri.host
                    && uri.port == clientRedirectUri.port
                ) {
                    return true
                }
                return false
            }

    }
}