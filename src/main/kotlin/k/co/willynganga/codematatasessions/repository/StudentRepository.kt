package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long> {

    fun findByEmail(email: String): Student?

    fun findByUsername(username: String): Student?

}