package k.co.willynganga.codematatasessions.service

import io.github.wickie73.mockito4kotlin.annotation.KCaptor
import io.github.wickie73.mockito4kotlin.annotation.KMockitoAnnotations
import k.co.willynganga.codematatasessions.model.ImageUrl
import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.repository.RecordingsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.KArgumentCaptor
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class RecordingServiceTest {

    @KCaptor
    private lateinit var stringArgumentCaptor: KArgumentCaptor<String>

    @Mock
    private lateinit var recordingsRepository: RecordingsRepository
    @Mock
    private lateinit var imageService: ImageService
    private lateinit var underTest: RecordingService

    @BeforeEach
    internal fun setUp() {
        KMockitoAnnotations.openMocks(this)
        underTest = RecordingService(recordingsRepository, imageService)
    }

    @Test
    fun `can find all recordings`() {
        //given
        val page = PageRequest.of(0, 12)
        whenever(recordingsRepository.findAll(page)).thenReturn(Page.empty())
        //when
        underTest.findAllRecordings(page)
        //then
        verify(recordingsRepository).findAll(page)
    }

    @Test
    fun `can find recording using valid Id`() {
        //given
        val id: Long = 1

        //when
        underTest.findRecordingById(id)

        //then
        val argumentCaptor = ArgumentCaptor.forClass(Long::class.java)
        verify(recordingsRepository).findById(argumentCaptor.capture())
        val capturedId = argumentCaptor.value

        assertEquals(capturedId, id)
    }

    @Test
    fun `can fnd recording by valid title`() {
        //given
        val title = "Spring Boot"
        //when
        underTest.findRecordingByTitle(title)
        //then
        verify(recordingsRepository).findByTitle(stringArgumentCaptor.capture())
        val capturedTitle = stringArgumentCaptor.firstValue

        assertEquals(capturedTitle, title)
    }

    @Test
    fun `can find recording using instructor username`() {
        //given
        val page = PageRequest.of(0, 12)
        val username = "test"
        whenever(recordingsRepository.findByInstructor(page, username)).thenReturn(Page.empty())
        //when
        underTest.findRecordingByInstructorUsername(page, username)

        //then
        val argumentCaptor = argumentCaptor<Pageable, String>()
        verify(recordingsRepository).findByInstructor(argumentCaptor.first.capture(), argumentCaptor.second.capture())
        val capturedUsername = argumentCaptor.second.firstValue

        assertEquals(capturedUsername, username)
    }

    @Test
    fun `can add recording`() {
        //given
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "06-05-2021",
            "test",
            )
        underTest.addRecording(recording)

        //when
        val argumentCaptor = ArgumentCaptor.forClass(Recording::class.java)
        verify(recordingsRepository).saveAndFlush(argumentCaptor.capture())
        val capturedRecording = argumentCaptor.value

        assertEquals(capturedRecording, recording)
    }

    @Test
    fun `can find recordings by date`() {
        //given
        val date = "2021-05-15"
        val page = PageRequest.of(0, 12)
        whenever(recordingsRepository.findByDate(page, date)).thenReturn(Page.empty())
        //when
        underTest.findRecordingByDate(page, date)

        //then
        val argumentCaptor = argumentCaptor<Pageable, String>()
        verify(recordingsRepository).findByDate(argumentCaptor.first.capture(), argumentCaptor.second.capture())
        val capturedDate = argumentCaptor.second.firstValue
        assertEquals(capturedDate, date)
    }

    @Test
    fun `can find recordings by title and date`() {
        //given
        val date = "2021-05-15"
        val title = "spring boot"
        val page = PageRequest.of(0, 12)
        whenever(recordingsRepository.findByTitleAndDate(page, title, date)).thenReturn(Page.empty())

        //when
        underTest.findRecordingByTitleAndDate(page, title, date)
        val argumentCaptor = argumentCaptor<Pageable, String>()
        verify(recordingsRepository).findByTitleAndDate(argumentCaptor.first.capture(), argumentCaptor.second.capture(), argumentCaptor.second.capture())

        //then
        val capturedTitle = argumentCaptor.second.firstValue
        val capturedDate = argumentCaptor.second.secondValue

        assertEquals(capturedTitle, title)
        assertEquals(capturedDate, date)
    }

    @Test
    fun `can delete recording with id`() {
        //given
        val id: Long = 1
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "https://www.youtube.com/embed/Geq60OVyBPg",
            "06-05-2021",
            "test",
            ImageUrl("http://localhost:8080/api/v1/images/1")
        )

        //when
        whenever(recordingsRepository.findById(id)).thenReturn(Optional.of(recording))
        whenever(imageService.deleteImage(id)).thenReturn(0)
        underTest.deleteRecordingById(id)

        //then
        val argumentCaptor = argumentCaptor<Recording>()
        verify(recordingsRepository).delete(argumentCaptor.capture())
        val capturedRecording = argumentCaptor.firstValue

        assertEquals(capturedRecording, recording)
    }
}