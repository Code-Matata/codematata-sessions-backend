package k.co.willynganga.codematatasessions.controller

import com.google.gson.Gson
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.security.oauth2.CustomOAuth2UserService
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationFailureHandler
import k.co.willynganga.codematatasessions.security.oauth2.OAuth2AuthenticationSuccessHandler
import k.co.willynganga.codematatasessions.service.OAuthUserService
import k.co.willynganga.codematatasessions.service.RecordingService
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
    private lateinit var recordingService: RecordingService



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