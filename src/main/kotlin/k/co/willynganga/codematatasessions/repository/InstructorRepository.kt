package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Instructor
import org.springframework.data.jpa.repository.JpaRepository

interface InstructorRepository : JpaRepository<Instructor, Long> {

    fun findByEmail(email: String): Instructor?

    fun findByUsername(username: String): Instructor?
}