package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.OAuthUser
import k.co.willynganga.codematatasessions.repository.OAuth2UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class OAuthUserService constructor(@Autowired private val repository: OAuth2UserRepository) {

    //find all
    fun findAllUsers(): List<OAuthUser> = repository.findAll()

    //find by email
    fun findByEmail(email: String): OAuthUser? = repository.findByEmail(email)

    //add user
    fun addUser(oAuthUser: OAuthUser) = repository.saveAndFlush(oAuthUser)

}