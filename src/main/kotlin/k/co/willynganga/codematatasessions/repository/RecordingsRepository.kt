package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Recording
import org.springframework.data.jpa.repository.JpaRepository

interface RecordingsRepository : JpaRepository<Recording, Long> {

    fun findByTitle(title: String): Recording?

}