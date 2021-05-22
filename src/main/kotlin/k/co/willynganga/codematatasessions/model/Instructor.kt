package k.co.willynganga.codematatasessions.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "instructors")
data class Instructor(
    @Id
    var uuid: String,
    @Column(unique = true)
    var username: String,
    @Column(unique = true)
    var email: String,
    var password: String,
    var avatar: String = ""
)
