package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.repository.RecordingsRepository
import org.springframework.stereotype.Service

@Service
open class RecordingService(private val repository: RecordingsRepository) {

    fun findAllRecordings(): List<Recording> {
        return repository.findAll();
    }

    fun findRecordingById(id: Long): Recording? {
        val recording = repository.findById(id)
        return if (recording.isPresent) recording.get() else null
    }

    fun findRecordingByTitle(title: String): Recording? {
        return repository.findByTitle(title)
    }
}