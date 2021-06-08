package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Event
import k.co.willynganga.codematatasessions.model.EventImageUrl
import k.co.willynganga.codematatasessions.repository.EventImageUrlRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class EventImageUrlServiceTest {

    @Mock
    private lateinit var eventImageUrlRepository: EventImageUrlRepository
    private lateinit var underTest: EventImageUrlService

    @BeforeEach
    internal fun setUp() {
        underTest = EventImageUrlService(eventImageUrlRepository)
    }

    @Test
    fun addUrl() {
        //given
        val url = "http://localhost:8080/api/v1/images/1"
        val event = Event(
            "PostgreSQL 101",
            "An intro to PostgreSQL as a relational DB.",
            LocalDateTime.parse("2021-06-08T10:15:30"),
            LocalDateTime.parse("2021-06-08T11:15:30"),
            "meet.google.com/kdj-jkxm-ntx",
            "Windows, Mac, or Linux OS."
        )

        //when
        underTest.addUrl(url, event)

        //then
        val argumentCaptor = argumentCaptor<EventImageUrl>()
        verify(eventImageUrlRepository).saveAndFlush(argumentCaptor.capture())
        val capturedEventImageUrl = argumentCaptor.firstValue

        assertEquals(capturedEventImageUrl.url, url)
    }
}