package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Instructor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
open class InstructorRepositoryTest @Autowired constructor(
    private val underTest: InstructorRepository
) {
    @BeforeEach
    internal fun setUp() {
        underTest.deleteAll()
    }

    @Test
    fun `it should find instructor with valid uuid`() {
        //given
        val uuid = "7f647610-5929-45e6-a1c6-4547e7918c36"
        val instructor = Instructor(
            uuid,
            "test",
            "test@test.com",
            "password"
        )
        underTest.saveAndFlush(instructor)

        //when
        val exists = underTest.findByUuid(uuid)

        assertThat(exists).isNotNull
    }

    @Test
    fun `it should not find instructor with invalid uuid`() {
        //given
        val uuid = "7f647610-5929-45e6-a1c6-4547e7918c36"
        val instructor = Instructor(
            uuid,
            "test",
            "test@test.com",
            "password"
        )
        underTest.saveAndFlush(instructor)

        //when
        val exists = underTest.findByUuid("8f647610-5929-45e6-a1c6-4547e7918c36")

        assertThat(exists).isNull()
    }

    @Test
    fun `it should find instructor with valid email`() {
        //given
        val email = "test@test.com"
        val instructor = Instructor(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            email,
            "password"
        )
        underTest.saveAndFlush(instructor)

        //when
        val exists = underTest.findByEmail(email)

        assertThat(exists).isNotNull
    }

    @Test
    fun `it should find instructor with valid username`() {
        //given
        val username = "test"
        val instructor = Instructor(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            username,
            "test@test.com",
            "password"
        )
        underTest.saveAndFlush(instructor)

        //when
        val exists = underTest.findByUsername(username)

        assertThat(exists).isNotNull
    }
}