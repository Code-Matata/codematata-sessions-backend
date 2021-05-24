package k.co.willynganga.codematatasessions.security.oauth2.user

abstract class OAuth2UserInfo(
    val attributes: Map<String, Any>
) {

    abstract fun getId(): String

    abstract fun getName(): String

    abstract fun getEmail(): String

    abstract fun getImageUrl(): String

}
