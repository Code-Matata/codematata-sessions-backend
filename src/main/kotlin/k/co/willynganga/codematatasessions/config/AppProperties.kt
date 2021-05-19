package k.co.willynganga.codematatasessions.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppProperties() {

    private val auth: Auth = Auth()
    private val oauth2: OAuth2 = OAuth2()

    data class Auth constructor(
        var tokenSecret: String = "",
        var tokenExpirationMsec: Long = 0
    )

    data class OAuth2 constructor(
        var authorizedRedirectUris: List<String> = listOf()
    )

    fun getAuth(): Auth = auth

    fun getOAuth2(): OAuth2 = oauth2
}