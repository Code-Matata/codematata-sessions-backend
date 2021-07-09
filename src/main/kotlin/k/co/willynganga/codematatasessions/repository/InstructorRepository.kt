package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Instructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InstructorRepository: JpaRepository<Instructor, Long> {

    fun findByUsername(username: String): Instructor?
}