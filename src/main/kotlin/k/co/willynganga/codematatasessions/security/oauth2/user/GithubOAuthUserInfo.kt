package k.co.willynganga.codematatasessions.security.oauth2.user

class GithubOAuthUserInfo(
    attributes: Map<String, Any>
) : OAuth2UserInfo(attributes) {

    override fun getId(): String = attributes["id"].toString()

    override fun getName(): String = attributes["name"].toString()

    override fun getEmail(): String = attributes["email"].toString()

    override fun getImageUrl(): String = attributes["avatar_url"].toString()
}
