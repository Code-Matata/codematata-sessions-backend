package k.co.willynganga.codematatasessions.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

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
    val instructor: String
)
