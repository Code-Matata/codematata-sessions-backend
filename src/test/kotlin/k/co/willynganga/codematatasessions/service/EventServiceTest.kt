package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Event
import k.co.willynganga.codematatasessions.model.EventImageUrl
import k.co.willynganga.codematatasessions.repository.EventsRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class EventServiceTest {

    @Mock
    private lateinit var eventsRepository: EventsRepository
    @Mock
    private lateinit var imageService: ImageService
    private lateinit var underTest: EventService

    @BeforeEach
    fun setUp() {
        underTest = EventService(eventsRepository, imageService)
    }

    @Test
    fun getAllEvents() {
        //given
        val pageable = PageRequest.of(0, 12)

        //when
        whenever(eventsRepository.findAll(pageable)).thenReturn(Page.empty())
        underTest.getAllEvents(pageable)

        //then
        val argumentCaptor = argumentCaptor<PageRequest>()
        verify(eventsRepository).findAll(argumentCaptor.capture())
        val capturedPageRequest = argumentCaptor.firstValue

        assertEquals(capturedPageRequest, pageable)
    }

    @Test
    fun `can get event with id`() {
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
        whenever(eventsRepository.findById(id)).thenReturn(Optional.of(event))
        underTest.getEvent(id)

        //then
        val argumentCaptor = argumentCaptor<Long>()
        verify(eventsRepository).findById(argumentCaptor.capture())
        val capturedId = argumentCaptor.firstValue

        assertEquals(capturedId, id)
    }

    @Test
    fun saveEvent() {
        //given
        val event = Event(
            "PostgreSQL 101",
            "An intro to PostgreSQL as a relational DB.",
            LocalDateTime.parse("2021-06-08T10:15:30"),
            LocalDateTime.parse("2021-06-08T11:15:30"),
            "meet.google.com/kdj-jkxm-ntx",
            "Windows, Mac, or Linux OS."
        )

        //when
        underTest.saveEvent(event)

        //then
        val argumentCaptor = argumentCaptor<Event>()
        verify(eventsRepository).saveAndFlush(argumentCaptor.capture())
        val capturedEvent = argumentCaptor.firstValue

        assertEquals(capturedEvent, event)
    }

    @Test
    fun `can delete event with valid id`() {
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
        val imageUrl = EventImageUrl(
            "http://localhost:8080/api/v1/images/1",
            event
        )
        event.imageUrl = imageUrl

        //when
        whenever(eventsRepository.findById(id)).thenReturn(Optional.of(event))
        whenever(imageService.deleteImage(id)).thenReturn(0)
        underTest.deleteEvent(id)

        //then
        val argumentCaptor = argumentCaptor<Event>()
        verify(eventsRepository).delete(argumentCaptor.capture())
        val capturedEvent = argumentCaptor.firstValue

        assertEquals(capturedEvent, event)
    }

    @Test
    fun `cannot delete event with invalid id`() {
        //given
        val id: Long = 1

        //when
        whenever(eventsRepository.findById(id)).thenReturn(Optional.empty())
        underTest.deleteEvent(id)

        //then
        verify(eventsRepository, never()).delete(any())
        verify(imageService, never()).deleteImage(any())
    }
}