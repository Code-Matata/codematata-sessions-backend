package k.co.willynganga.codematatasessions.other

class Constants {

    companion object {
        const val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request"
        const val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri"
        const val cookieExpireSeconds = 180
        const val MAX_AGE_SECS: Long = 3600
        const val IMAGE_BASE_URL = "https://code-matata.herokuapp.com/api/v1/images/"
    }
}