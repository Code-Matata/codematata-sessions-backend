package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Event
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventsRepository: JpaRepository<Event, Long> {
}