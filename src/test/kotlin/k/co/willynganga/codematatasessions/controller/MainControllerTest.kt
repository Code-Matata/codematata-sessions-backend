package k.co.willynganga.codematatasessions.controller

import com.google.gson.Gson
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import k.co.willynganga.codematatasessions.model.Instructor
import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.model.Student
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationFailureHandler
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationSuccessHandler
import k.co.willynganga.codematatasessions.service.*
import k.co.willynganga.codematatasessions.util.STATUS
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
internal class MainControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var customOAuth2UserService: CustomOAuth2UserService
    @MockkBean
    private lateinit var oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler
    @MockkBean
    private lateinit var oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler
    @MockkBean
    private lateinit var oAuthUserService: OAuthUserService

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

    @Test
    fun `can get all instructors`() {
        //given
        val instructor = Instructor(
            "8bf81f1a-270d-46d3-98bf-52b05f58eb4a",
            "test",
            "test@test.com",
            "password"
        )

        //when
        every { instructorService.findAllInstructors() } returns listOf(instructor)

        //then
        mockMvc.perform(get("/api/v1/instructor/all"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$[0].uuid").value(instructor.uuid))
            .andExpect(jsonPath("\$[0].email").value(instructor.email))
            .andExpect(jsonPath("\$[0].username").value(instructor.username))
    }

    @Test
    fun `can register instructor`() {
        //given
        val instructor = Instructor(
            "8bf81f1a-270d-46d3-98bf-52b05f58eb4a",
            "test",
            "test@test.com",
            "password"
        )
        val body = Gson().toJson(instructor)

        //when
        every { instructorService.insertInstructor(instructor) } returns Response(
            0,
            STATUS.SUCCESS,
            "Successfully added recording"
        )

        //then
        mockMvc.perform(
            post("/api/v1/instructor/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.requestCode").value(0))
            .andExpect(jsonPath("\$.message").value("Successfully added recording"))
    }

    @Test
    fun `cannot add instructor with email or username that is taken`() {
        //given
        val instructor = Instructor(
            "8bf81f1a-270d-46d3-98bf-52b05f58eb4a",
            "test",
            "test@test.com",
            "password"
        )
        val body = Gson().toJson(instructor)

        //when
        every { instructorService.insertInstructor(instructor) } returns Response(
            1,
            STATUS.FAIL,
            "Email and username need to be unique"
        )

        //then
        mockMvc.perform(
            post("/api/v1/instructor/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(body)
                .characterEncoding("utf-8")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.requestCode").value(1))
            .andExpect(jsonPath("\$.message").value("Email and username need to be unique"))
    }

    @Test
    fun `can get instructor with valid email`() {
        //given
        val email = "test@test.com"
        val instructor = Instructor(
            "8bf81f1a-270d-46d3-98bf-52b05f58eb4a",
            "test",
            email,
            "password"
        )

        //when
        every { instructorService.findInstructorByEmail(email) } returns instructor

        //then
        mockMvc.perform(
            get("/api/v1/instructor")
                .param("email", email)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.uuid").value(instructor.uuid))
            .andExpect(jsonPath("\$.email").value(instructor.email))
            .andExpect(jsonPath("\$.username").value(instructor.username))
    }

    @Test
    fun `can get instructor with valid uuid`() {
        //given
        val uuid = "8bf81f1a-270d-46d3-98bf-52b05f58eb4a"
        val instructor = Instructor(
            uuid,
            "test",
            "test@test.com",
            "password"
        )

        //when
        every { instructorService.findInstructorByUuid(uuid) } returns instructor

        //then
        mockMvc.perform(
            get("/api/v1/instructor/uuid/$uuid")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.uuid").value(instructor.uuid))
            .andExpect(jsonPath("\$.email").value(instructor.email))
            .andExpect(jsonPath("\$.username").value(instructor.username))
    }

    @Test
    fun `can get instructor with valid username`() {
        //given
        val username = "test"
        val instructor = Instructor(
            "8bf81f1a-270d-46d3-98bf-52b05f58eb4a",
            username,
            "test@test.com",
            "password"
        )

        //when
        every { instructorService.findInstructorByUsername(username) } returns instructor

        //then
        mockMvc.perform(
            get("/api/v1/instructor/username/$username")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.uuid").value(instructor.uuid))
            .andExpect(jsonPath("\$.email").value(instructor.email))
            .andExpect(jsonPath("\$.username").value(instructor.username))
    }

    @Test
    fun `can get all recordings`() {
        //given
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            "Jane Doe",
            1,
            )

        //when
        every { recordingService.findAllRecordings() } returns listOf(recording)

        //then
        mockMvc.perform(
            get("/api/v1/recording/all")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.[0].title").value(recording.title))
    }

    @Test
    fun `can add a recording`() {
        //given
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            "Jane Doe",
            1
            )
        val body = Gson().toJson(recording)
        //when
        every { recordingService.addRecording(recording) } returns Response(
            0,
            STATUS.SUCCESS,
            "Recording Successfully Saved!"
        )

        //then
        mockMvc.perform(
            post("/api/v1/recording/add")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.requestCode").value(0))
            .andExpect(jsonPath("\$.message").value("Recording Successfully Saved!"))
    }

    @Test
    fun `can get recording by id`() {
        //given
        val id: Long = 1
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            "Jane Doe",
            id
            )

        //when
        every { recordingService.findRecordingById(id) } returns recording

        //then
        mockMvc.perform(
            get("/api/v1/recording/$id")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(recording.id))
            .andExpect(jsonPath("\$.title").value(recording.title))
    }

    @Test
    fun `can get recording by title`() {
        //given
        val title = "Spring Boot"
        val recording = Recording(
            title,
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            "Jane Doe",
            1,
            )

        //when
        every { recordingService.findRecordingByTitle(title) } returns recording

        //then
        mockMvc.perform(
            get("/api/v1/recording/by-title")
                .param("title", title)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.id").value(recording.id))
            .andExpect(jsonPath("\$.title").value(recording.title))
    }

    @Test
    fun `can get recordings by instructor username`() {
        //given
        val username = "Jane Doe"
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            username,
            1,
            )

        //when
        every { recordingService.findRecordingByInstructorUsername(username) } returns  listOf(recording)

        //then
        mockMvc.perform(
            get("/api/v1/recording/by-instructor")
                .param("username", username)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.[0].title").value(recording.title))
    }

    @Test
    fun `can get recording by date`() {
        //given
        val date = "06-05-2021"
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            date,
            "test",
            1,
            )

        //when
        every { recordingService.findRecordingByDate(date) } returns listOf(recording)

        //then
        mockMvc.perform(
            get("/api/v1/recording/by-date")
                .param("date", date)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.[0].title").value(recording.title))
    }

    @Test
    fun `can get recording by title and date`() {
        //given
        val date = "06-05-2021"
        val title = "Spring Boot"
        val recording = Recording(
            title,
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            date,
            "test",
            1
        )

        //when
        every { recordingService.findRecordingByTitleAndDate(title, date) } returns listOf(recording)

        //then
        mockMvc.perform(
            get("/api/v1/recording/by-title-and-date")
                .param("title", title)
                .param("date", date)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.[0].title").value(recording.title))
    }
}