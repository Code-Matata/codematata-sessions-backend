package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.EventImageUrl
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventImageUrlRepository: JpaRepository<EventImageUrl, Long> {
}