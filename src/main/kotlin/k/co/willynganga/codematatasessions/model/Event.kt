package k.co.willynganga.codematatasessions.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "session_event")
data class Event(
    val title: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val meetUrl: String,
    val prerequisites: String,
    @OneToOne(
        mappedBy = "event",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var imageUrl: EventImageUrl? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)