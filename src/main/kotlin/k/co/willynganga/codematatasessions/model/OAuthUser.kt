package k.co.willynganga.codematatasessions.model

import k.co.willynganga.codematatasessions.other.ROLE
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "oauth_users")
data class OAuthUser(
    @Id
    @Column(length = 30)
    val sub: String,
    @Column(length = 100)
    val name: String,
    @Column(length = 50)
    val given_name: String,
    @Column(length = 50)
    val family_name: String,
    val picture: String,
    @Column(length = 50)
    val email: String,
    @Column(length = 10)
    val role: ROLE
)
