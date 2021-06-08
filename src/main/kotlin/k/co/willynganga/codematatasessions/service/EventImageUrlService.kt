package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Event
import k.co.willynganga.codematatasessions.model.EventImageUrl
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.EventImageUrlRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.stereotype.Service

@Service
class EventImageUrlService(
    private val eventImageUrlRepository: EventImageUrlRepository
){

    fun addUrl(url: String, event: Event): Response {
        eventImageUrlRepository.saveAndFlush(EventImageUrl(url, event))
        return Response(0, STATUS.SUCCESS, "Url added successfully!")
    }

}