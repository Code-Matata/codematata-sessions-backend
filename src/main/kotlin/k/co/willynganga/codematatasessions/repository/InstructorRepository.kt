package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Instructor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InstructorRepository : JpaRepository<Instructor, Long> {

    fun findByUuid(uuid: String): Instructor?

    fun findByEmail(email: String): Instructor?

    fun findByUsername(username: String): Instructor?

    @Query("SELECT i FROM Instructor i WHERE i.email = ?1 OR i.username = ?2")
    fun findInstructorByEmailOrUsername(email: String, username: String): List<Instructor>
}