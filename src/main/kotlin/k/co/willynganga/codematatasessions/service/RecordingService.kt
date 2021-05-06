package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.repository.RecordingsRepository
import k.co.willynganga.codematatasessions.util.STATUS
import org.springframework.stereotype.Service

@Service
open class RecordingService(
    private val repository: RecordingsRepository
) {

    fun findAllRecordings(): List<Recording> {
        return repository.findAll();
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

    fun findRecordingByInstructorUsername(username: String): Recording? {
        return repository.findByInstructor(username)
    }
}