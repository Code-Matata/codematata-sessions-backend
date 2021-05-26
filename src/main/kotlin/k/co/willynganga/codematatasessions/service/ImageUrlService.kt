package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.ImageUrl
import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.ImageUrlRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.stereotype.Service

@Service
class ImageUrlService(
    private val imageUrlRepository: ImageUrlRepository
){

    fun addUrl(url: String, recording: Recording): Response {
        imageUrlRepository.saveAndFlush(ImageUrl(url, recording))
        return Response(0, STATUS.SUCCESS, "Url added successfully!")
    }

    fun getUrl(id: Long): String {
        val existingUrl = imageUrlRepository.findById(id)
        return existingUrl.map { it.url }.orElse(null)
    }

    fun deleteUrl(id: Long): Response {
        imageUrlRepository.deleteById(id)
        return Response(0, STATUS.SUCCESS, "Deleted the image url!")
    }

}