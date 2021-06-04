package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Recording
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest

@DataJpaTest
open class RecordingsRepositoryTest @Autowired constructor(
    private val underTest: RecordingsRepository
) {

    @BeforeEach
    internal fun setUp() {
        underTest.deleteAll()
        underTest.flush()
    }

    @Test
    fun `it should find recording with valid title`() {
        //given
        val title = "Spring Boot"
        val recording = Recording(
            title,
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "06-05-2021",
            "test"
        )
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
            title,
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
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
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "06-05-2021",
            username
        )
        val page = PageRequest.of(0, 12)

        //when
        underTest.saveAndFlush(recording)

        //then
        val exists = underTest.findByInstructor(page ,username)

        assertThat(exists.content).isNotNull
    }

    @Test
    fun `it should find recordings by date`() {
        //given
        val date = "06-05-2021"
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            date,
            "test"
        )
        val page = PageRequest.of(0, 12)
        //when
        underTest.saveAndFlush(recording)

        //then
        val exists = underTest.findByDate(page, date)

        assertEquals(exists.content, listOf(recording))
    }

    @Test
    fun `it should not find recordings with invalid date`() {
        //given
        val date = "12-12-2020"
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "2021-05-06",
            "test"
        )
        val page = PageRequest.of(0, 12)
        //when
        underTest.saveAndFlush(recording)

        //then
        val exists = underTest.findByDate(page, date)

        assertThat(exists.content).isEmpty()
    }

    @Test
    fun `it should find recordings by title and date`() {
        //given
        val date = "06-05-2021"
        val title = "Spring Boot"
        val recording = Recording(
            title,
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            date,
            "test"
        )
        val page = PageRequest.of(0, 12)
        //when
        underTest.saveAndFlush(recording)

        //then
        val exists = underTest.findByTitleAndDate(page, title, date)

        assertEquals(exists.content, listOf(recording))
    }

    @Test
    fun `it should not find recordings with invalid title and date`() {
        //given
        val date = "06-05-20211"
        val title = "spring boot"
        val recording = Recording(
            "Spring Boot",
            "An introduction to spring boot and Kotlin",
            "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/Geq60OVyBPg\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>",
            "06-05-2021",
            "test"
        )
        val page = PageRequest.of(0, 12)
        //when
        underTest.saveAndFlush(recording)

        //then
        val exists = underTest.findByTitleAndDate(page, title, date)

        assertThat(exists.content).isEmpty()
    }
}