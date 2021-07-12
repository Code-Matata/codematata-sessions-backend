package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Instructor
import k.co.willynganga.codematatasessions.model.InstructorsResponse
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.InstructorRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class InstructorService(private val repository: InstructorRepository) {

    fun findByUsername(username: String): Instructor? {
        return repository.findByUsername(username)
    }

    fun getAllInstructors(pageable: Pageable): InstructorsResponse {
        val page = repository.findAll(pageable)
        return InstructorsResponse(
            page.totalPages,
            page.number,
            page.content
        )
    }

    fun addInstructor(instructor: Instructor): Response {
        repository.saveAndFlush(instructor)
        return Response(0, STATUS.SUCCESS, "Instructor saved successfully!")
    }

    fun updateInstructor(instructor: Instructor): Response {
        val existingInstructor = repository.findById(instructor.id)
        return if (existingInstructor.isPresent) {
            repository.save(instructor)
            Response(0, STATUS.SUCCESS, "Instructor saved successfully!")
        } else {
            Response(1, STATUS.FAIL, "Instructor could not be found!")
        }
    }

    fun deleteInstructor(username: String): Response {
        val instructor = repository.findByUsername(username)
        instructor?.let {
            repository.delete(it)
            return Response(0, STATUS.SUCCESS, "Instructor deleted successfully!")
        }
        return Response(1, STATUS.FAIL, "No instructor matches username $username!")
    }
}