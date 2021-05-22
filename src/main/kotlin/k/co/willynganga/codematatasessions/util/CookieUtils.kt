package k.co.willynganga.codematatasessions.util

import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.util.SerializationUtils
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CookieUtils {

    companion object {
        fun getCookie(request: HttpServletRequest, name: String): Cookie? {
            val cookies = request.cookies

            if (cookies != null && cookies.isNotEmpty()) {
                cookies.iterator()
                    .forEach { cookie ->
                        if (cookie.name == name) {
                            return cookie
                        }
                    }
            }
            return null
        }

        fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
            val cookie = Cookie(name, value)
            cookie.apply {
                path = "/"
                isHttpOnly = true
                this.maxAge = maxAge
            }
            response.addCookie(cookie)
        }

        fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
            val cookies = request.cookies
            if (cookies != null && cookies.isNotEmpty()) {
                cookies.iterator()
                    .forEach { cookie ->
                        if (cookie.name == name) {
                            cookie.value = ""
                            cookie.path = "/"
                            cookie.maxAge = 0
                            response.addCookie(cookie)
                        }
                    }
            }
        }

        fun serialize(ob: OAuth2AuthorizationRequest): String {
            return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(ob))
        }

        fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
            return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.value)))
        }
    }

}