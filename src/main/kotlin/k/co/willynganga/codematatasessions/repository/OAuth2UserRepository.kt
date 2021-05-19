package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.OAuthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OAuth2UserRepository : JpaRepository<OAuthUser, String> {

    fun findBySub(sub: String): OAuthUser?

    fun findByEmail(email: String): OAuthUser?

    fun deleteBySub(sub: String)

    fun existsBySub(sub: String): Boolean
}