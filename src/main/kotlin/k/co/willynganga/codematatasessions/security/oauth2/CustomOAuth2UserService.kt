package k.co.willynganga.codematatasessions.security.oauth2

import k.co.willynganga.codematatasessions.exception.OAuth2AuthenticationProcessingException
import k.co.willynganga.codematatasessions.model.OAuthUser
import k.co.willynganga.codematatasessions.other.PROVIDER
import k.co.willynganga.codematatasessions.security.UserPrincipal
import k.co.willynganga.codematatasessions.security.oauth2.user.OAuth2UserInfo
import k.co.willynganga.codematatasessions.security.oauth2.user.OAuth2UserInfoFactory
import k.co.willynganga.codematatasessions.service.OAuthUserService
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(private val oAuthUserService: OAuthUserService) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest?): OAuth2User {
        val oauth2User = super.loadUser(userRequest)

        try {
            return processOAuth2User(userRequest, oauth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(userRequest: OAuth2UserRequest?, oauth2User: OAuth2User?): OAuth2User {
        val oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            userRequest?.clientRegistration?.registrationId!!,
            oauth2User?.attributes!!
        )
        if (oAuth2UserInfo.getEmail().isEmpty()) {
            throw OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider")
        }

        var user = oAuthUserService.findByEmail(oAuth2UserInfo.getEmail())

        return if (user != null) {
            UserPrincipal.create(user, oauth2User.attributes)
        } else {
            user = registerNewUser(userRequest, oAuth2UserInfo)
            UserPrincipal.create(user, oauth2User.attributes)
        }
    }

    private fun registerNewUser(userRequest: OAuth2UserRequest, oAuth2UserInfo: OAuth2UserInfo): OAuthUser {
        val user = OAuthUser(
            provider = PROVIDER.valueOf(userRequest.clientRegistration.registrationId.toUpperCase()),
            providerId = oAuth2UserInfo.getId(),
            name = oAuth2UserInfo.getName(),
            email = oAuth2UserInfo.getEmail(),
            imageUrl = oAuth2UserInfo.getImageUrl()
        )
        oAuthUserService.addUser(user)
        return user
    }
}