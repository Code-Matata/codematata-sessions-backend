package k.co.willynganga.codematatasessions.security

import k.co.willynganga.codematatasessions.exception.ResourceNotFoundException
import k.co.willynganga.codematatasessions.repository.OAuth2UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private var oAuth2UserRepository: OAuth2UserRepository): UserDetailsService {

    override fun loadUserByUsername(email: String?): UserDetails {
        oAuth2UserRepository.findByEmail(email!!)?.let {
            return UserPrincipal.create(it)
        }
        throw UsernameNotFoundException("User not found with email: $email")
    }

    fun loadUserById(id: Long): UserDetails {
        val user = oAuth2UserRepository.findById(id).orElseThrow {
            ResourceNotFoundException("User id $id")
        }

        return UserPrincipal.create(user)
    }
}