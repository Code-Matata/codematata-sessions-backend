package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Instructor
import k.co.willynganga.codematatasessions.model.Recording
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
open class RecordingsRepositoryTest @Autowired constructor(
    private val underTest: RecordingsRepository,
    private val instructorRepository: InstructorRepository
) {

    @BeforeEach
    internal fun setUp() {
        underTest.deleteAll()
    }

    @Test
    fun `it should find recording with valid title`() {
        //given
        val title = "Spring Boot"
        val instructor = Instructor(
            "849fd6dd-5ae1-47f0-9640-b3cb34a9392a",
            "test",
            "test@test.com",
            "password"
        )
        val recording = Recording(
            1,
            title,
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            "test"
        )
        instructorRepository.save(instructor)
        underTest.save(recording)
        //when
        val exists = underTest.findByTitle(title)

        //then
        assertThat(exists).isNotNull
    }

    @Test
    fun `it should not find recording with invalid title`() {
        //given
        val title = "Spring Boot"
        val recording = Recording(
            1,
            title,
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            "test"
        )
        underTest.save(recording)
        //when
        val exists = underTest.findByTitle("spring boot")

        //then
        assertThat(exists).isNull()
    }

    @Test
    fun `find recording using instructor username`() {
        //given
        val username = "test"
        val recording = Recording(
            1,
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "https://youtu.be/Geq60OVyBPg",
            "06-05-2021",
            username
        )

        //when
        underTest.saveAndFlush(recording)

        //then
        val exists = underTest.findByInstructor(username)

        assertThat(exists).isNotNull
    }
}