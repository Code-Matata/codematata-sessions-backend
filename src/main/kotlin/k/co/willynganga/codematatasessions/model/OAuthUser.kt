package k.co.willynganga.codematatasessions.model

import k.co.willynganga.codematatasessions.other.PROVIDER
import javax.persistence.*

@Entity(name = "oauth_users")
data class OAuthUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(length = 100)
    val name: String,
    val imageUrl: String,
    @Column(length = 50)
    val email: String,
    val emailVerified: Boolean = false,
    val provider: PROVIDER,
    val providerId: String

)
