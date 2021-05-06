package k.co.willynganga.codematatasessions.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "instructors")
data class Instructor(
    @Id
    val uuid: String,
    @Column(unique = true)
    val username: String,
    @Column(unique = true)
    val email: String,
    val password: String
)
