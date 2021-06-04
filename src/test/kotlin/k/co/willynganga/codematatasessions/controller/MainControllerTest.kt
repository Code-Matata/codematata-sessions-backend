package k.co.willynganga.codematatasessions.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import k.co.willynganga.codematatasessions.model.*
import k.co.willynganga.codematatasessions.other.PROVIDER
import k.co.willynganga.codematatasessions.security.TokenAuthenticationFilter
import k.co.willynganga.codematatasessions.security.oauth2.CustomOAuth2UserService
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationFailureHandler
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationSuccessHandler
import k.co.willynganga.codematatasessions.service.ImageService
import k.co.willynganga.codematatasessions.service.ImageUrlService
import k.co.willynganga.codematatasessions.service.OAuthUserService
import k.co.willynganga.codematatasessions.service.RecordingService
import k.co.willynganga.codematatasessions.util.STATUS
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.core.io.ClassPathResource
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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
}