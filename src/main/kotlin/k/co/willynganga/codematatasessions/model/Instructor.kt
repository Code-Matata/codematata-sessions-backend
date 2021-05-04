package k.co.willynganga.codematatasessions.model

import javax.persistence.*

@Entity
@Table(name = "instructors")
data class Instructor(
    @Id
    val uuid: String,
    @Column(unique = true)
    val username: String,
    @Column(unique = true)
    val email: String,
    val password: String,
    @OneToMany(mappedBy = "instructor")
    val sessions: Set<Recording>
)
