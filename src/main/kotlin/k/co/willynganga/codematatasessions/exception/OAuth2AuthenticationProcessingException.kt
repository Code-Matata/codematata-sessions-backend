package k.co.willynganga.codematatasessions.exception

import javax.naming.AuthenticationException

class OAuth2AuthenticationProcessingException(
    msg: String
) : AuthenticationException(msg)
