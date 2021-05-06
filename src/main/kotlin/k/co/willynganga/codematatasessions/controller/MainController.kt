package k.co.willynganga.codematatasessions.controller

import k.co.willynganga.codematatasessions.model.Instructor
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.model.Student
import k.co.willynganga.codematatasessions.service.InstructorService
import k.co.willynganga.codematatasessions.service.RecordingService
import k.co.willynganga.codematatasessions.service.StudentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
open class MainController(
    private val studentService: StudentService,
    private val instructorService: InstructorService,
    private val recordingService: RecordingService
) {

    //Student
    @GetMapping("student/all")
    fun getAllStudents(): List<Student> {
        return studentService.findAllStudents();
    }

    @PostMapping("/student/add")
    fun addStudent(@RequestBody student: Student): Response {
        return studentService.insertStudent(student)
    }

    @GetMapping("/student/email")
    fun getStudentByEmail(@RequestParam email: String): Student? {
        return studentService.findStudentByEmail(email)
    }

    @GetMapping("/student/username")
    fun getStudentByUsername(@RequestParam username: String): Student? {
        return studentService.findStudentByUsername(username)
    }

    @GetMapping("/student/uuid")
    fun getStudentByUuid(@RequestParam uuid: String): Student? {
        return studentService.findStudentByUuid(uuid)
    }

    //instructor
    @GetMapping("/instructor/all")
    fun getAllInstructors(): List<Instructor> {
        return instructorService.findAllInstructors()
    }

    @PostMapping("/instructor/add")
    fun addInstructor(@RequestBody instructor: Instructor): Response {
        return instructorService.insertInstructor(instructor)
    }

    @GetMapping("/instructor")
    fun getInstructorByEmail(@RequestParam email: String): Instructor? {
        return instructorService.findInstructorByEmail(email)
    }

    @GetMapping("/instructor/uuid/{uuid}")
    fun getInstructorByUuid(@PathVariable uuid: String): Instructor? {
        return instructorService.findInstructorByUuid(uuid)
    }

    @GetMapping("/instructor/username/{username}")
    fun getInstructorByUsername(@PathVariable username: String): Instructor? {
        return instructorService.findInstructorByUsername(username)
    }
}