package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Recording
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RecordingsRepository : JpaRepository<Recording, Long> {

    fun findByTitle(title: String): Recording?

    fun findByDate(pageable: Pageable, date: String): Page<Recording>

    fun findByTitleAndDate(pageable: Pageable, title: String, date: String): Page<Recording>
}