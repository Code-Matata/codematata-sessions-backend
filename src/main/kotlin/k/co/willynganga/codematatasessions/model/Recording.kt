package k.co.willynganga.codematatasessions.model

import javax.persistence.*

@Entity
@Table(name = "recordings")
data class Recording(
    @Id
    val id: Long,
    val title: String,
    val description: String,
    val videoUrl: String,
    val imageUrl: String,
    val date: String,
    @ManyToOne
    @JoinColumn(name = "instructor_uuid")
    val instructor: Instructor
)
