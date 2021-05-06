package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Instructor
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.InstructorRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.stereotype.Service

@Service
open class InstructorService(private val repository: InstructorRepository) {

    fun findAllInstructors(): List<Instructor> {
        return repository.findAll();
    }

    fun insertInstructor(instructor: Instructor): Response {
        val existingInstructor = repository.findInstructorByEmailOrUsername(instructor.email, instructor.username)
        if (existingInstructor.isNotEmpty()) {
            return Response(
                1, STATUS.FAIL,
                "Email and password need to be unique"
            )
        }
        repository.saveAndFlush(instructor)
        return Response(
        0, STATUS.SUCCESS,
            "Successfully added recording"
        )
    }

    fun findInstructorByEmail(email: String): Instructor? {
        return repository.findByEmail(email)
    }

    fun findInstructorByUsername(username: String): Instructor? {
        return repository.findByUsername(username)
    }

    fun findInstructorByUuid(uuid: String): Instructor? {
        return repository.findByUuid(uuid)
    }

}