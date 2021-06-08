package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Event
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.EventsRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.stereotype.Service

@Service
class EventService(val eventsRepository: EventsRepository, val imageService: ImageService) {

    fun getAllEvents(): List<Event> = eventsRepository.findAll()

    fun getEvent(id: Long): Event? = eventsRepository.findById(id).get()

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