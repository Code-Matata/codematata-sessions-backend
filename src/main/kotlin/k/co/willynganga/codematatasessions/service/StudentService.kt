package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.model.Student
import k.co.willynganga.codematatasessions.repository.StudentRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.stereotype.Service

@Service
open class StudentService(private val repository: StudentRepository) {

    fun findAllStudents(): List<Student> {
        return repository.findAll();
    }

    fun insertStudent(student: Student): Response {
        val existingStudents = repository.findStudentByEmailOrUsername(student.email, student.username)
        if (existingStudents.isNotEmpty()) {
            return Response(1, STATUS.FAIL, "Your email and username need to be unique")
        }
        repository.saveAndFlush(student)
        return Response(0, STATUS.SUCCESS, "Successfully added.")
    }

    fun findStudentByEmail(email: String): Student? {
        return repository.findByEmail(email)
    }

    fun findStudentByUsername(username: String): Student? {
        return repository.findByUsername(username)
    }

    fun findStudentByUuid(uuid: String): Student? {
        return repository.findStudentByUuid(uuid)
    }

}