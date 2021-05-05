package k.co.willynganga.codematatasessions.service

import io.github.wickie73.mockito4kotlin.annotation.KCaptor
import io.github.wickie73.mockito4kotlin.annotation.KMockitoAnnotations
import k.co.willynganga.codematatasessions.model.Student
import k.co.willynganga.codematatasessions.repository.StudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.KArgumentCaptor

@ExtendWith(MockitoExtension::class)
internal class StudentServiceTest {
    @KCaptor
    lateinit var stringArgumentCaptor: KArgumentCaptor<String>

    @Mock
    private lateinit var studentRepository: StudentRepository
    private lateinit var underTest: StudentService

    @BeforeEach
    internal fun setUp() {
        KMockitoAnnotations.openMocks(this)
        underTest = StudentService(studentRepository)
    }

    @Test
    fun `can get all students`() {
        // when
        underTest.findAllStudents()
        //then
        verify(studentRepository).findAll()
    }

    @Test
    fun `can add student`() {
        //given
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            "test@test.com",
            "password"
        )
        //when
        underTest.insertStudent(student)

        //then
        val studentArgumentCaptor = ArgumentCaptor.forClass(Student::class.java)

        verify(studentRepository).saveAndFlush(studentArgumentCaptor.capture())

        val capturedStudent = studentArgumentCaptor.value

        assertThat(capturedStudent).isEqualTo(student)
    }

    @Test
    fun `will not add student when email or username is taken`() {
        //given
        val student = Student(
            "7f647610-5929-45e6-a1c6-4547e7918c36",
            "test",
            "test@test.com",
            "password"
        )
        underTest.insertStudent(student)

        given(studentRepository.findStudentByEmailOrUsername(anyString(), anyString()))
            .willReturn(listOf(student))

        //when
        //then
        assertThat(underTest.insertStudent(student))
            .hasFieldOrPropertyWithValue("requestCode", 1)
    }

    @Test
    fun findStudentByEmail() {
        //given
        val email = "test@test.com"

        //when
        underTest.findStudentByEmail(email)

        //then
        verify(studentRepository).findByEmail(stringArgumentCaptor.capture())
        val capturedEmail = stringArgumentCaptor.firstValue

        assertEquals(capturedEmail, email)
    }

    @Test
    fun `can find student with username`() {
        //given
        val username = "test"

        //when
        underTest.findStudentByUsername(username)

        //then
        verify(studentRepository).findByUsername(stringArgumentCaptor.capture())
        val capturedUsername = stringArgumentCaptor.firstValue

        assertEquals(capturedUsername, username)
    }

    @Test
    fun `can find student with uuid`() {
        //given
        val uuid = "7f647610-5929-45e6-a1c6-4547e7918c36"

        //when
        underTest.findStudentByUuid(uuid)

        //then
        verify(studentRepository).findStudentByUuid(stringArgumentCaptor.capture())
        val capturedUUID = stringArgumentCaptor.firstValue

        assertEquals(capturedUUID, uuid)
    }
}