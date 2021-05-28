package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Image
import k.co.willynganga.codematatasessions.repository.ImageRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ImageService(
    private val imageRepository: ImageRepository
) {

    private val logger = LoggerFactory.getLogger(ImageService::class.java)

    fun findImageById(id: Long): Image? = imageRepository.findById(id).orElse(null)

    fun addImage(bytes: ByteArray): Image? {
        val image = Image(bytes)
        imageRepository.saveAndFlush(image)
        return image
    }

    fun deleteImage(id: Long): Long {
        val image = imageRepository.findById(id).orElse(null)
        if (image != null) {
            imageRepository.delete(image)
            return 0
        }
        return 1
    }

}