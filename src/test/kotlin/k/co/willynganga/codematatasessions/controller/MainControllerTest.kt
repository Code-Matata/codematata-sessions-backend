package k.co.willynganga.codematatasessions.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import k.co.willynganga.codematatasessions.model.*
import k.co.willynganga.codematatasessions.other.Constants.Companion.IMAGE_BASE_URL
import k.co.willynganga.codematatasessions.other.PROVIDER
import k.co.willynganga.codematatasessions.security.TokenAuthenticationFilter
import k.co.willynganga.codematatasessions.security.oauth2.CustomOAuth2UserService
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationFailureHandler
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationSuccessHandler
import k.co.willynganga.codematatasessions.service.*
import k.co.willynganga.codematatasessions.util.STATUS
import k.co.willynganga.codematatasessions.util.Utils.Companion.convertFileToBytes
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
internal class MainControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var imageService: ImageService

    @MockkBean
    private lateinit var imageUrlService: ImageUrlService

    @MockkBean
    private lateinit var tokenAuthenticationFilter: TokenAuthenticationFilter

    @MockkBean
    private lateinit var customOAuth2UserService: CustomOAuth2UserService

    @MockkBean
    private lateinit var oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler

    @MockkBean
    private lateinit var oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler

    @MockkBean
    private lateinit var oAuthUserService: OAuthUserService

    @MockkBean
    private lateinit var recordingService: RecordingService

    @MockkBean
    private lateinit var eventService: EventService

    @MockkBean
    private lateinit var eventImageUrlService: EventImageUrlService


    @Test
    fun `can get all recordings`() {
        //given
        val page = PageRequest.of(0, 12)
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "06-05-2021",
            "Jane Doe",
            ImageUrl("http://localhost/api/v1/images/1", Recording("", "", "", "", "")),
            1,
        )

        //when
        every { recordingService.findAllRecordings(page) } returns RecordingsResponse(
            1,
            0,
            listOf(recording)
        )

        //then
        mockMvc.perform(
            get("/api/v1/recording/all")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.recordings.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.recordings.[0].title").value(recording.title))
    }

    @Test
    fun `can add a recording`() {
        //given
        val image = ClassPathResource("application-local-img.png")
        val file = MockMultipartFile("file", image.inputStream)
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "06-05-2021",
            "Jane Doe"
        )

        //when
        every { recordingService.addRecording(recording) } returns Response(
            0,
            STATUS.SUCCESS,
            "Recording Successfully Saved!"
        )
        every { imageService.addImage(image.inputStream.readAllBytes()) } returns Image(image.inputStream.readAllBytes())
        every {
            imageUrlService.addUrl(
                "https://code-matata.herokuapp.com/api/v1/images/0",
                recording
            )
        } returns Response(
            0,
            STATUS.SUCCESS,
            "Url added successfully!"
        )
        //then
        mockMvc.perform(
            multipart("/api/v1/recording/add")
                .file(file)
                .param("title", recording.title)
                .param("description", recording.description)
                .param("videoUrl", recording.videoUrl)
                .param("date", recording.date)
                .param("instructor", recording.instructor)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `can get recording by id`() {
        //given
        val id: Long = 1
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "06-05-2021",
            "Jane Doe",
            ImageUrl("http://localhost/api/v1/images/1", Recording("", "", "", "", "")),
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
            "06-05-2021",
            "Jane Doe",
            ImageUrl("http://localhost/api/v1/images/1", Recording("", "", "", "", "")),
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
            "06-05-2021",
            username,
            ImageUrl("http://localhost/api/v1/images/1", Recording("", "", "", "", "")),
            1,
        )
        val page = PageRequest.of(0, 12)
        //when
        every { recordingService.findRecordingByInstructorUsername(page, username) } returns RecordingsResponse(
            1,
            0,
            listOf(recording)
        )

        //then
        mockMvc.perform(
            get("/api/v1/recording/by-instructor")
                .param("username", username)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.recordings.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.recordings.[0].title").value(recording.title))
    }

    @Test
    fun `can get recording by date`() {
        //given
        val date = "06-05-2021"
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            date,
            "test",
            ImageUrl("http://localhost/api/v1/images/1", Recording("", "", "", "", "")),
            1,
        )
        val page = PageRequest.of(0, 12)
        //when
        every { recordingService.findRecordingByDate(page ,date) } returns RecordingsResponse(
            1,
            0,
            listOf(recording)
        )


        //then
        mockMvc.perform(
            get("/api/v1/recording/by-date")
                .param("date", date)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.recordings.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.recordings.[0].title").value(recording.title))
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
            date,
            "test",
            ImageUrl("http://localhost/api/v1/images/1", Recording("", "", "", "", "")),
            1
        )
        val page = PageRequest.of(0, 12)

        //when
        every { recordingService.findRecordingByTitleAndDate(page, title, date) } returns RecordingsResponse(
            1,
            0,
            listOf(recording)
        )


        //then
        mockMvc.perform(
            get("/api/v1/recording/by-title-and-date")
                .param("title", title)
                .param("date", date)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.recordings.[0].id").value(recording.id))
            .andExpect(jsonPath("\$.recordings.[0].title").value(recording.title))
    }

    @Test
    fun `can get all OAuth2 users`() {
        //given
        val oAuthUser = OAuthUser(
            "Jane Doe",
            "https://localhost:8080/img/1",
            "jane.doe@gmail.com",
            true,
            PROVIDER.GOOGLE,
            "1234567890"
        )

        //when
        every { oAuthUserService.findAllUsers() } returns listOf(oAuthUser)

        //then
        mockMvc.perform(
            get("/api/v1/oauth-user/all")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.[0].id").value(oAuthUser.id))
            .andExpect(jsonPath("\$.[0].name").value(oAuthUser.name))
            .andExpect(jsonPath("\$.[0].providerId").value(oAuthUser.providerId))
    }

    @Test
    fun `can get image with id`() {
        //given
        val id: Long = 1
        val image = Image(ByteArray(100), id)
        //when
        every { imageService.findImageById(id) } returns image

        //then
        mockMvc.perform(
            get("/api/v1/images/1")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
    }

    @Test
    fun `can return empty list of event`() {
        //given
        val pageable = PageRequest.of(0, 12)

        //when
        every { eventService.getAllEvents(pageable) } returns EventsResponse(0, 0, emptyList())

        //then
        mockMvc.perform(
            get("/api/v1/event/all")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.pages").value(0))
            .andExpect(jsonPath("\$.currentPage").value(0))
    }

    @Test
    fun `can get all events`() {
        //given
        val event = Event(
            "PostgreSQL 101",
            "An intro to PostgreSQL as a relational DB.",
            LocalDateTime.parse("2021-06-08T10:15:30"),
            LocalDateTime.parse("2021-06-08T11:15:30"),
            "meet.google.com/kdj-jkxm-ntx",
            "Windows, Mac, or Linux OS."
        )
        val pageable = PageRequest.of(0, 12)

        //when
        every { eventService.getAllEvents(pageable) } returns EventsResponse(1, 0, listOf(event))

        //then
        mockMvc.perform(
            get("/api/v1/event/all")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.pages").value(1))
            .andExpect(jsonPath("\$.currentPage").value(0))
            .andExpect(jsonPath("\$.events.[0].title").value(event.title))
            .andExpect(jsonPath("\$.events.[0].meetUrl").value(event.meetUrl))
    }

    @Test
    fun `can save a new event`() {
        //given
        val image = ClassPathResource("application-local-img.png")
        val file = MockMultipartFile("file", image.inputStream)
        val event = Event(
            "PostgreSQL 101",
            "An intro to PostgreSQL as a relational DB.",
            LocalDateTime.parse("2021-06-08T10:15:30"),
            LocalDateTime.parse("2021-06-08T11:15:30"),
            "meet.google.com/kdj-jkxm-ntx",
            "Windows, Mac, or Linux OS."
        )

        //when
        every { eventService.saveEvent(event) } returns Response(0, STATUS.SUCCESS, "Event added successfully!")
        every { imageService.addImage(convertFileToBytes(file)!!) } returns Image(convertFileToBytes(file)!!)
        every { eventImageUrlService.addUrl(IMAGE_BASE_URL + "0", event) } returns Response(0, STATUS.SUCCESS, "Url added successfully!")

        //then
        mockMvc.perform(
            multipart("/api/v1/event/add")
                .file(file)
                .param("title", event.title)
                .param("description", event.description)
                .param("startTime", "2021-06-08T10:15:30")
                .param("endTime", "2021-06-08T11:15:30")
                .param("meetUrl", event.meetUrl)
                .param("prerequisites", event.prerequisites)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.requestCode").value(0))
            .andExpect(jsonPath("\$.message").value("Event added successfully!"))
    }

    @Test
    fun `can get event with a valid id`() {
        //given
        val id: Long = 1
        val event = Event(
            "PostgreSQL 101",
            "An intro to PostgreSQL as a relational DB.",
            LocalDateTime.parse("2021-06-08T10:15:30"),
            LocalDateTime.parse("2021-06-08T11:15:30"),
            "meet.google.com/kdj-jkxm-ntx",
            "Windows, Mac, or Linux OS."
        )
        //when
        every { eventService.getEvent(id) } returns event

        //then
        mockMvc.perform(
            get("/api/v1/event/1")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.title").value(event.title))
            .andExpect(jsonPath("\$.meetUrl").value(event.meetUrl))
    }

    @Test
    fun `can delete event by id`() {
        //given
        val id: Long = 1

        //when
        every { eventService.deleteEvent(id) } returns Response(0, STATUS.SUCCESS, "Event deleted successfully!")

        //then
        mockMvc.perform(
            delete("/api/v1/event/delete/1")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("\$.requestCode").value(0))
            .andExpect(jsonPath("\$.message").value("Event deleted successfully!"))
    }
}