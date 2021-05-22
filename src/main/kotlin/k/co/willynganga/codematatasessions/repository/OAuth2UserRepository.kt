package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.OAuthUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OAuth2UserRepository : JpaRepository<OAuthUser, Long> {

    fun findByEmail(email: String): OAuthUser?


}