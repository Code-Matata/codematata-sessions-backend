package k.co.willynganga.codematatasessions.security.oauth2

import com.nimbusds.oauth2.sdk.util.StringUtils
import k.co.willynganga.codematatasessions.other.Constants.Companion.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
import k.co.willynganga.codematatasessions.other.Constants.Companion.REDIRECT_URI_PARAM_COOKIE_NAME
import k.co.willynganga.codematatasessions.other.Constants.Companion.cookieExpireSeconds
import k.co.willynganga.codematatasessions.util.CookieUtils
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class HttpCookieOAuth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    override fun loadAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest {
        return CookieUtils.getCookie(request!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME).let {
            CookieUtils.deserialize(it!!, OAuth2AuthorizationRequest::class.java)
        }
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request!!, response!!, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME)
            return
        }

        CookieUtils.addCookie(
            response!!,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(authorizationRequest),
            cookieExpireSeconds
        )
        val redirectedUriAfterLogin = request!!.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME)
        if (StringUtils.isNotBlank(redirectedUriAfterLogin)) {
            CookieUtils.addCookie(
                response,
                REDIRECT_URI_PARAM_COOKIE_NAME,
                redirectedUriAfterLogin,
                cookieExpireSeconds
            )
        }
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest?
    ): OAuth2AuthorizationRequest {
        return this.loadAuthorizationRequest(request)
    }

    fun removeAuthorizedRequestCookies(request: HttpServletRequest, response: HttpServletResponse) {
        CookieUtils.deleteCookie(
            request,
            response,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
        )
        CookieUtils.deleteCookie(
            request,
            response,
            REDIRECT_URI_PARAM_COOKIE_NAME
        )
    }
}