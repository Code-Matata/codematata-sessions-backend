package k.co.willynganga.codematatasessions.service

import io.github.wickie73.mockito4kotlin.annotation.KMockitoAnnotations
import k.co.willynganga.codematatasessions.model.OAuthUser
import k.co.willynganga.codematatasessions.other.PROVIDER
import k.co.willynganga.codematatasessions.repository.OAuth2UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
internal class OAuthUserServiceTest {

    @Mock
    private lateinit var oAuth2UserRepository: OAuth2UserRepository

    private lateinit var underTest: OAuthUserService

    @BeforeEach
    internal fun setUp() {
        KMockitoAnnotations.openMocks(this)
        underTest = OAuthUserService(oAuth2UserRepository)
    }

    @Test
    fun findAllUsers() {
        //when
        underTest.findAllUsers()

        //then
        verify(oAuth2UserRepository).findAll()
    }

    @Test
    fun findByEmail() {
        //given
        val email = "jane.doe@gmail.com"

        //when
        underTest.findByEmail(email)

        //then
        val argumentCaptor = argumentCaptor<String>()
        verify(oAuth2UserRepository).findByEmail(argumentCaptor.capture())

        val capturedEmail = argumentCaptor.firstValue
        assertEquals(capturedEmail, email)
    }

    @Test
    fun addUser() {
        //given
        val oAuthUser = OAuthUser(
            "Jane Doe",
            "https://localhost:8080/img/1",
            "jane.doe@gmail.com",
            true,
            PROVIDER.GOOGLE,
            "1234567890"
        )

        //when
        underTest.addUser(oAuthUser)

        //then
        val argumentCaptor = argumentCaptor<OAuthUser>()
        verify(oAuth2UserRepository).saveAndFlush(argumentCaptor.capture())

        val capturedOAuthUser = argumentCaptor.firstValue
        assertEquals(capturedOAuthUser, oAuthUser)
    }

    @Test
    fun deleteUserByEmail() {
        //given
        val email = "jane.doe@gmail.com"

        //when
        underTest.deleteUserByEmail(email)

        //then
        val argumentCaptor = argumentCaptor<String>()
        verify(oAuth2UserRepository).deleteByEmail(argumentCaptor.capture())

        val capturedEmail = argumentCaptor.firstValue
        assertEquals(capturedEmail, email)

    }
}