package k.co.willynganga.codematatasessions.controller

import k.co.willynganga.codematatasessions.model.*
import k.co.willynganga.codematatasessions.service.InstructorService
import k.co.willynganga.codematatasessions.service.OAuthUserService
import k.co.willynganga.codematatasessions.service.RecordingService
import k.co.willynganga.codematatasessions.service.StudentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/")
open class MainController(
    private val studentService: StudentService,
    private val instructorService: InstructorService,
    private val recordingService: RecordingService,
    private val oAuthUserService: OAuthUserService
) {

    //Student
    @GetMapping("student/all")
    fun getAllStudents(): List<Student> {
        return studentService.findAllStudents()
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

    //Recording
    @GetMapping("/recording/all")
    fun getAllRecordings(): List<Recording> {
        return recordingService.findAllRecordings()
    }

    @PostMapping("/recording/add")
    fun addRecording(@RequestBody recording: Recording): Response {
        return recordingService.addRecording(recording)
    }

    @GetMapping("/recording/{id}")
    fun getRecordingById(@PathVariable id: Long): Recording? {
        return recordingService.findRecordingById(id)
    }

    @GetMapping("/recording/by-title")
    fun getRecordingByTitle(@RequestParam title: String): Recording? {
        return recordingService.findRecordingByTitle(title)
    }

    @GetMapping("/recording/by-instructor")
    fun getRecordingByInstructorUsername(@RequestParam username: String): List<Recording> {
        return recordingService.findRecordingByInstructorUsername(username)
    }

    @GetMapping("/recording/by-date")
    fun getRecordingByDate(@RequestParam("date") date: String): List<Recording> {
        return recordingService.findRecordingByDate(date)
    }

    @GetMapping("/recording/by-title-and-date")
    fun getRecordingByTitleAndString(@RequestParam("title") title: String,
                                     @RequestParam("date") date: String): List<Recording> {
        return recordingService.findRecordingByTitleAndDate(title, date)
    }

    //OAuthUser

    @GetMapping("/oauth-user/all")
    fun getAllOAuthUsers(): List<OAuthUser> = oAuthUserService.findAllUsers()
}