package k.co.willynganga.codematatasessions.security

import k.co.willynganga.codematatasessions.model.OAuthUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    private val id: Long,
    private val email: String,
    private val authorities: MutableCollection<out  GrantedAuthority>
) : OAuth2User, UserDetails {

    private var attributes: MutableMap<String, Any> = mutableMapOf()

    companion object {

        fun create(user: OAuthUser, attributes: MutableMap<String, Any>): UserPrincipal {
            val userPrinciple = UserPrincipal.create(user)
            userPrinciple.attributes = attributes
            return userPrinciple
        }

        fun create(user: OAuthUser): UserPrincipal {
            val authorities = mutableListOf<GrantedAuthority>(SimpleGrantedAuthority("ROLE_USER"))

            return UserPrincipal(
                user.id,
                user.email,
                authorities
            )
        }
    }

    fun getId(): Long = id

    override fun getName(): String = id.toString()

    override fun getAttributes(): MutableMap<String, Any> = attributes

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getPassword(): String = ""

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}