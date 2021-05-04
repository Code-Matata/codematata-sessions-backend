package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface StudentRepository : JpaRepository<Student, Long> {

    fun findStudentByUuid(uuid: String): Student?

    fun findByEmail(email: String): Student?

    fun findByUsername(username: String): Student?

    @Query("SELECT s FROM Student s WHERE s.email = ?1 OR s.username = ?2")
    fun findStudentByEmailOrUsername(email: String, username: String): List<Student>

}