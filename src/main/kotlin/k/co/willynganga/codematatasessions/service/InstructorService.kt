package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Instructor
import k.co.willynganga.codematatasessions.repository.InstructorRepository
import org.springframework.stereotype.Service

@Service
open class InstructorService(private val repository: InstructorRepository) {

    fun findAllInstructors(): List<Instructor> {
        return repository.findAll();
    }

    fun findInstructorByEmail(email: String): Instructor? {
        return repository.findByEmail(email)
    }

    fun findInstructorByUsername(username: String): Instructor? {
        return repository.findByUsername(username)
    }
}