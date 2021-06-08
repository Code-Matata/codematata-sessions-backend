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

    fun getUrl(id: Long): String {
        val existingUrl = eventImageUrlRepository.findById(id)
        return existingUrl.map { it.url }.orElse(null)
    }

    fun deleteUrl(id: Long): Response {
        eventImageUrlRepository.deleteById(id)
        return Response(0, STATUS.SUCCESS, "Deleted the image url!")
    }

}