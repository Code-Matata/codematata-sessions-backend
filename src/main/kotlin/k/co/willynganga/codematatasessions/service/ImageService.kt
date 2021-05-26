package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Image
import k.co.willynganga.codematatasessions.repository.ImageRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(
    private val imageRepository: ImageRepository
) {

    private val logger = LoggerFactory.getLogger(ImageService::class.java)

    fun findImageById(id: Long): Image = imageRepository.findById(id).orElse(null)

    fun addImage(file: MultipartFile): Image? {
        try {
            val bytes = file.bytes
            val image = Image(bytes)
            imageRepository.saveAndFlush(image)
            return image
        } catch (t: Throwable) {
            logger.error(t.message)
        }

        return null
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