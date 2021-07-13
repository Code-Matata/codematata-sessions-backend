package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Event
import k.co.willynganga.codematatasessions.model.EventDto
import k.co.willynganga.codematatasessions.model.EventsResponse
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.EventsRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class EventService(val eventsRepository: EventsRepository, val imageService: ImageService) {

    fun getAllEvents(pageable: Pageable): EventsResponse {
        val page = eventsRepository.findAll(pageable)
        val eventsDto = page.content.map { EventDto(it) }.sortedByDescending { it.startTime }
        return EventsResponse(
            page.totalPages,
            page.number,
            eventsDto
        )
    }

    fun getEvent(id: Long): Event? = eventsRepository.findById(id).orElse(null)

    fun saveEvent(event: Event): Response {
        eventsRepository.saveAndFlush(event)
        return Response(0, STATUS.SUCCESS, "Event saved successfully!")
    }

    fun deleteEvent(id: Long): Response {
        val event = eventsRepository.findById(id)
        event.ifPresent {
            imageService.deleteImage(it.imageUrl?.url?.takeLast(1)?.toLong()!!)
            eventsRepository.delete(it)
        }

        return if (event.isPresent)
            Response(0, STATUS.SUCCESS, "Event deleted successfully!")
        else
            Response(1, STATUS.FAIL, "No such event exists!")
    }

}