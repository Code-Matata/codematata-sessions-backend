package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.model.RecordingsResponse
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.RecordingsRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
open class RecordingService(
    private val repository: RecordingsRepository,
    private val imageService: ImageService
) {

    fun findAllRecordings(pageable: Pageable): RecordingsResponse {
        val page = repository.findAll(pageable)
        return RecordingsResponse(
            page.totalPages,
            page.number,
            page.content
        )
    }

    fun addRecording(recording: Recording): Response {
        repository.saveAndFlush(recording)
        return Response(0, STATUS.SUCCESS, "Recording Successfully Saved!")
    }

    fun findRecordingById(id: Long): Recording? {
        val recording = repository.findById(id)
        return if (recording.isPresent) recording.get() else null
    }

    fun findRecordingByTitle(title: String): Recording? {
        return repository.findByTitle(title)
    }

    fun findRecordingByDate(pageable: Pageable, date: String): RecordingsResponse {
        val page = repository.findByDate(pageable, date)
        return RecordingsResponse(
            page.totalPages,
            page.number,
            page.content
        )
    }

    fun findRecordingByTitleAndDate(pageable: Pageable, title: String, date: String): RecordingsResponse {
        val page = repository.findByTitleAndDate(pageable, title, date)
        return RecordingsResponse(
            page.totalPages,
            page.number,
            page.content
        )
    }

    fun deleteRecordingById(id: Long): Response {
        val recording = repository.findById(id)
        recording.ifPresent {
            imageService.deleteImage(it.imageUrl?.url?.takeLast(1)?.toLong()!!)
            repository.delete(it)
        }
        return if (recording.isPresent)
            Response(0, STATUS.SUCCESS, "Recording deleted successfully!")
        else Response(1, STATUS.FAIL, "There is no event with id of $id!")
    }
}