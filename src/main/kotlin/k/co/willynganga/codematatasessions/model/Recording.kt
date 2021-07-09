package k.co.willynganga.codematatasessions.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.persistence.*

@Entity
@Table(name = "recordings")
data class Recording(
    val title: String,
    val description: String,
    val videoUrl: String,
    val git: String,
    val date: String,
    @OneToOne(
        mappedBy = "recording",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    var imageUrl: ImageUrl? = null,
    @JsonIgnoreProperties("recordings")
    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    var instructor: Instructor? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)
