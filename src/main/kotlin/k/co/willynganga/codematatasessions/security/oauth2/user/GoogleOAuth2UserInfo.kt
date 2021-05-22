package k.co.willynganga.codematatasessions.security.oauth2.user

class GoogleOAuth2UserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {

    override fun getId(): String = attributes["sub"].toString()

    override fun getName(): String = attributes["name"].toString()

    override fun getEmail(): String = attributes["email"].toString()

    override fun getImageUrl(): String = attributes["picture"].toString()
}
