package k.co.willynganga.codematatasessions.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "session_event")
open class Event(
    open val title: String,
    open val description: String,
    open val startTime: LocalDateTime,
    open val endTime: LocalDateTime,
    open val meetUrl: String,
    open val prerequisites: String,
    @OneToOne(
        mappedBy = "event",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    open var imageUrl: EventImageUrl? = null,
    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    open var instructor: Instructor? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0
)