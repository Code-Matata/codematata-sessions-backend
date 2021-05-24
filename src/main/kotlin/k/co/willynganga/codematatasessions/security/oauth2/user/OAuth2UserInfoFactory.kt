package k.co.willynganga.codematatasessions.security.oauth2.user

import k.co.willynganga.codematatasessions.other.PROVIDER

class OAuth2UserInfoFactory {
    companion object {
        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            return if (registrationId.equals(PROVIDER.GOOGLE.toString(), true)) {
                GoogleOAuth2UserInfo(attributes)
            } else {
                GithubOAuthUserInfo(attributes)
            }
        }
    }
}