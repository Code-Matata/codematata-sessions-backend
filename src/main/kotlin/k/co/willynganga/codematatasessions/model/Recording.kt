package k.co.willynganga.codematatasessions.model

import javax.persistence.*

@Entity
@Table(name = "recordings")
data class Recording(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,
    val description: String,
    val videoUrl: String,
    val imageUrl: String,
    val date: String,
    val instructor: String
)
