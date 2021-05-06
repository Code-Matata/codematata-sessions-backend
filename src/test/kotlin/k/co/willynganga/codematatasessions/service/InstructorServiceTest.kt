package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.Instructor
import k.co.willynganga.codematatasessions.repository.InstructorRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.given
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class InstructorServiceTest {

    @Mock
    private lateinit var instructorRepository: InstructorRepository
    private lateinit var underTest: InstructorService

    @BeforeEach
    internal fun setUp() {
        underTest = InstructorService(instructorRepository)
    }

    @Test
    fun `can find all instructors`() {
        //when
        underTest.findAllInstructors()

        //then
        verify(instructorRepository).findAll()
    }

    @Test
    fun `can add instructor`() {
        //given
        val instructor = Instructor(
            "",
            "test",
            "test@test.com",
            "password"
        )

        //when
        underTest.insertInstructor(instructor)

        //then
        val argumentCaptor = argumentCaptor<Instructor>()
        verify(instructorRepository).saveAndFlush(argumentCaptor.capture())
        val capturedInstructor = argumentCaptor.firstValue

        assertEquals(capturedInstructor, instructor)
    }

    @Test
    @Disabled
    fun `will not add instructor when email or username is taken`() {
        //given
        val instructor = Instructor(
            "",
            "test",
            "test@test.com",
            "password"
        )
        underTest.insertInstructor(instructor)

        given(instructorRepository.findInstructorByEmailOrUsername(any(), any()))
            .willReturn(listOf(instructor))

        //when
        //then
        assertThat(underTest.insertInstructor(instructor))
            .hasFieldOrPropertyWithValue("requestCode", 1)
    }

    @Test
    @Disabled
    fun `can find instructor with email`() {
        //given
        val email = "test@test.com"

        //when
        underTest.findInstructorByEmail(email)

        //then
        val argumentCaptor = argumentCaptor<String>()
        verify(instructorRepository).findByEmail(argumentCaptor.capture())
        val capturedEmail = argumentCaptor.firstValue

        assertEquals(capturedEmail, email)
    }

    @Test
    fun `can find instructor with valid username`() {
        //given
        val username = "test"

        //when
        underTest.findInstructorByUsername(username)

        //then
        val argumentCaptor = argumentCaptor<String>()
        verify(instructorRepository).findByUsername(argumentCaptor.capture())
        val capturedUsername = argumentCaptor.firstValue

        assertEquals(capturedUsername, username)
    }

    @Test
    @Disabled
    fun `can find instructor valid uuid`() {
        //given
        val uuid = "7f647610-5929-45e6-a1c6-4547e7918c36"

        //when
        underTest.findInstructorByUuid(uuid)

        //then
        val argumentCaptor = argumentCaptor<String>()
        verify(instructorRepository).findByUsername(argumentCaptor.capture())
        val capturedUsername = argumentCaptor.firstValue

        assertEquals(capturedUsername, uuid)
    }
}