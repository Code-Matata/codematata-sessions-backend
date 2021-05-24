package k.co.willynganga.codematatasessions.model

import k.co.willynganga.codematatasessions.other.PROVIDER
import javax.persistence.*

@Entity
@Table(name = "aouth_users")
data class OAuthUser(
    @Column(length = 100)
    val name: String,
    val imageUrl: String,
    @Column(length = 50)
    val email: String,
    val emailVerified: Boolean = false,
    val provider: PROVIDER,
    val providerId: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
)
