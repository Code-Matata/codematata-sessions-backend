package k.co.willynganga.codematatasessions.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User constructor(private val oAuth2User: OAuth2User): OAuth2User {

    override fun getName(): String {
        return oAuth2User.name
    }

    override fun getAttributes(): MutableMap<String, Any> {
        return oAuth2User.attributes
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return oAuth2User.authorities
    }
}