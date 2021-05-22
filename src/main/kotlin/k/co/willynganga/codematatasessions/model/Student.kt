package k.co.willynganga.codematatasessions.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "students")
data class Student(
    @Id
    val uuid: String,
    @Column(unique = true)
    val username: String,
    @Column(unique = true)
    val email: String,
    val password: String,
    val avatar: String = ""
)