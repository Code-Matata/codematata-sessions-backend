package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.OAuthUser
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.OAuth2UserRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OAuthUserService constructor(@Autowired private val repository: OAuth2UserRepository) {

    //find all
    fun findAllUsers(): List<OAuthUser> = repository.findAll()

    //find by email
    fun findByEmail(email: String): OAuthUser? = repository.findByEmail(email)

    //find by Id
    fun findById(id: Long) = repository.findById(id)

    //add user
    fun addUser(oAuthUser: OAuthUser): Response {
        repository.saveAndFlush(oAuthUser)
        return Response(0, STATUS.SUCCESS, "User added successfully!")
    }

    //delete user
    fun deleteUserByEmail(email: String) = repository.deleteByEmail(email)

}