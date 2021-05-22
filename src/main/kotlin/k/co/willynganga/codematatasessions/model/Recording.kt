package k.co.willynganga.codematatasessions.model

import javax.persistence.*

@Entity
@Table(name = "recordings")
data class Recording(
    val title: String,
    val description: String,
    val videoUrl: String,
    val imageUrl: String,
    val date: String,
    val instructor: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
)
