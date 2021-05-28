package k.co.willynganga.codematatasessions.util

import org.slf4j.LoggerFactory
import org.springframework.web.multipart.MultipartFile
import java.io.IOException

class Utils {

    companion object{
        private val logger = LoggerFactory.getLogger(Utils::class.java)

        fun convertFileToBytes(file: MultipartFile): ByteArray? {
            try {
                return file.bytes
            } catch (ex: IOException) {
                logger.error(ex.message)
            }
            return null
        }
    }
}