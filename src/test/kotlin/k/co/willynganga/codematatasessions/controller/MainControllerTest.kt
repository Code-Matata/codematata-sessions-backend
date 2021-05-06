package k.co.willynganga.codematatasessions.controller

import com.google.gson.Gson
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.model.Student
import k.co.willynganga.codematatasessions.service.InstructorService
import k.co.willynganga.codematatasessions.service.RecordingService
import k.co.willynganga.codematatasessions.service.StudentService
import k.co.willynganga.codematatasessions.util.STATUS
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
internal class MainControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var studentService: StudentService

    @MockkBean
    private lateinit var instructorService: InstructorService

    @MockkBean
    private lateinit var recordingService: RecordingService

    @Test
    fun `get all students`() {
        //given
        val student1 = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            "test@test.com",
            "password"
        )
        val student2 = Student(
            "8f647610-5929-45e6-a1c6-4547e7918c36",
            "jane",
            "jane@test.com",
            "password"
        )

        //when
        every { studentService.findAllStudents() } returns listOf(student1, student2)

        //then
        mockMvc.perform(get("/api/v1/student/all"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].uuid").value(student1.uuid))
            .andExpect(jsonPath("\$.[1].email").value(student2.email))
            .andExpect(jsonPath("\$.[0].username").value(student1.username))
    }

    @Test
    fun `register student`() {
        //given
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            "test@test.com",
            "password"
        )
        val body = Gson().toJson(student)
        //when
        every { studentService.insertStudent(student) } returns Response(
            0,
            STATUS.SUCCESS,
            "Student added successfully"
        )

        //then
        mockMvc.perform(
            post("/api/v1/student/add")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.requestCode").value(0))
            .andExpect(jsonPath("\$.message").value("Student added successfully"))
    }

    @Test
    fun `fail to register student`() {
        //given
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            "test@test.com",
            "password"
        )
        val body = Gson().toJson(student)
        //when
        every { studentService.insertStudent(student) } returns Response(
            1,
            STATUS.FAIL,
            "Student username and email need to be unique"
        )

        //then
        mockMvc.perform(
            post("/api/v1/student/add")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.requestCode").value(1))
            .andExpect(jsonPath("\$.message").value("Student username and email need to be unique"))
    }

    @Test
    fun `fetch user with valid email`() {
        //given
        val email = "test@test.com"
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            email,
            "password"
        )
        //when
        every { studentService.findStudentByEmail(email) } returns student
        //then
        mockMvc.perform(
            get("/api/v1/student/email").param("email", "test@test.com")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.uuid").value(student.uuid))
            .andExpect(jsonPath("\$.email").value(student.email))
            .andExpect(jsonPath("\$.username").value(student.username))
    }

    @Test
    fun `fetch user with valid username`() {
        //given
        val username = "test"
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            username,
            "test@test.com",
            "password"
        )
        //when
        every { studentService.findStudentByUsername(username) } returns student
        //then
        mockMvc.perform(
            get("/api/v1/student/username").param("username", "test")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.uuid").value(student.uuid))
            .andExpect(jsonPath("\$.email").value(student.email))
            .andExpect(jsonPath("\$.username").value(student.username))
    }

    @Test
    fun `fetch user with valid uuid`() {
        //given
        val uuid = "7f647610-5929-45e6-a1c6-4547e7918c36"
        val student = Student(
            uuid,
            "test",
            "test@test.com",
            "password"
        )
        //when
        every { studentService.findStudentByUuid(uuid) } returns student
        //then
        mockMvc.perform(
            get("/api/v1/student/uuid")
                .param("uuid", "7f647610-5929-45e6-a1c6-4547e7918c36")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.uuid").value(student.uuid))
            .andExpect(jsonPath("\$.email").value(student.email))
            .andExpect(jsonPath("\$.username").value(student.username))
    }
}