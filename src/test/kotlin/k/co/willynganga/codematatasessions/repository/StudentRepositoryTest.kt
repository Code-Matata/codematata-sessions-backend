package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.Student
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
open class StudentRepositoryTest @Autowired constructor(private val underTest: StudentRepository) {

    @AfterEach
    fun tearDown() {
        underTest.deleteAll()
    }

    @Test
    fun `it should find student with valid uuid`() {
        //given
        val uuid = "7f647610-5929-45e6-a1c6-4547e7918c36"
        val student = Student(
            uuid,
            "test",
            "test@test.com",
            "password"
        )
        underTest.save(student)

        //when
        val exists = underTest.findStudentByUuid(uuid)

        //then
        assertThat(exists).isNotNull
    }

    @Test
    fun `it should not find student with invalid uuid`() {
        //given
        val uuid = "7f647610-5929-45e6-a1c6-4547e7918c36"

        //when
        val exists = underTest.findStudentByUuid(uuid)

        //then
        assertThat(exists).isNull()
    }

    @Test
    fun `it should find student with valid email`() {
        //given
        val email = "test@test.com"
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            email,
            "password"
        )
        underTest.save(student)

        //when
        val exists = underTest.findByEmail(email)

        //then
        assertThat(exists).isNotNull
    }

    @Test
    fun `it should not find student with invalid email`() {
        //given
        val email = "test@test.com"

        //when
        val exists = underTest.findByEmail(email)

        //then
        assertThat(exists).isNull()
    }

    @Test
    fun `it should find student with valid username`() {
        //given
        val username = "test"
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            username,
            "test@test.com",
            "password"
        )
        underTest.save(student)

        //when
        val exists = underTest.findByUsername(username)

        //then
        assertThat(exists).isNotNull
    }

    @Test
    fun `it should not find student with invalid username`() {
        //given
        val username = "tset"

        //when
        val exists = underTest.findByUsername(username)

        //then
        assertThat(exists).isNull()
    }

    @Test
    fun `it should not save if there is another entity with same username or email`() {
        //given
        val username = "test"
        val email = "test@test.com"
        val student1 = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            username,
            email,
            "password"
        )
        underTest.save(student1)
        val username2 = "test"
        val email2 = "demo@demo.com"
        val student2 = Student(
            "5d4d5231-fa06-47a7-899d-11c100ec63ca",
            username2,
            email2,
            "password"
        )

        //when
        val list = underTest.findStudentByEmailOrUsername(email2, username2)
        if (list.isEmpty()) {
            underTest.save(student2)
        }

        //then
        assertThat(list.size < 2).isTrue
    }
}