package k.co.willynganga.codematatasessions.model

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
data class EventImageUrl(
    var url: String,
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_event_id", nullable = false)
    @JsonBackReference
    val event: Event,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0
)
