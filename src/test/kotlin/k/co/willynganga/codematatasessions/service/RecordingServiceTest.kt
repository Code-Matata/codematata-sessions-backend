package k.co.willynganga.codematatasessions.service

import io.github.wickie73.mockito4kotlin.annotation.KCaptor
import io.github.wickie73.mockito4kotlin.annotation.KMockitoAnnotations
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

@ExtendWith(MockitoExtension::class)
internal class RecordingServiceTest {

    @KCaptor
    private lateinit var stringArgumentCaptor: KArgumentCaptor<String>

    @Mock
    private lateinit var recordingsRepository: RecordingsRepository
    private lateinit var underTest: RecordingService

    @BeforeEach
    internal fun setUp() {
        KMockitoAnnotations.openMocks(this)
        underTest = RecordingService(recordingsRepository)
    }

    @Test
    fun `can find all recordings`() {
        //when
        underTest.findAllRecordings()
        //then
        verify(recordingsRepository).findAll()
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
        val username = "test"
        underTest.findRecordingByInstructorUsername(username)

        //when
        verify(recordingsRepository).findByInstructor(stringArgumentCaptor.capture())
        val capturedUsername = stringArgumentCaptor.firstValue

        assertEquals(capturedUsername, username)
    }

    @Test
    fun `can add recording`() {
        //given
        val recording = Recording(
            1,
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            "test"
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

        underTest.findRecordingByDate(date)

        //when
        val argumentCaptor = argumentCaptor<String>()
        verify(recordingsRepository).findByDate(argumentCaptor.capture())

        //then
        val capturedDate = argumentCaptor.firstValue
        assertEquals(capturedDate, date)
    }

    @Test
    fun `can find recordings by title and date`() {
        //given
        val date = "2021-05-15"
        val title = "spring boot"

        underTest.findRecordingByTitleAndDate(title, date)

        //when
        val argumentCaptor = argumentCaptor<String>()
        verify(recordingsRepository).findByTitleAndDate(argumentCaptor.capture(), argumentCaptor.capture())

        //then
        val capturedTitle = argumentCaptor.firstValue
        val capturedDate = argumentCaptor.secondValue

        assertEquals(capturedTitle, title)
        assertEquals(capturedDate, date)
    }
}