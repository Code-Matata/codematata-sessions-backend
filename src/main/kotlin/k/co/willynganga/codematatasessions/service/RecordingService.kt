package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.repository.RecordingsRepository
import org.springframework.stereotype.Service

@Service
open class RecordingService(private val repository: RecordingsRepository) {

    fun findAllRecorings(): List<Recording> {
        return repository.findAll();
    }

    fun findRecordingById(uuid: String): Recording? {
        return repository.findById(uuid)
    }

    fun findRecordingByTitle(title: String): Recording? {
        return repository.findByTitle(title)
    }
}