package k.co.willynganga.codematatasessions.model

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "instructors")
data class Instructor(
    @Column(unique = true)
    val username: String,
    val name: String,
    @Column(unique = true)
    val email: String,
    val bioInfo: String = "",
    val github: String = "",
    val twitter: String = "",
    val instagram: String = "",
    val phone: String = "",
    @OneToMany(
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY
    )
    @JsonIgnore
    val recordings: List<Recording> = emptyList(),
    @OneToMany(
        mappedBy = "instructor"
    )
    @JsonIgnore
    val events: List<Event> = emptyList(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)
