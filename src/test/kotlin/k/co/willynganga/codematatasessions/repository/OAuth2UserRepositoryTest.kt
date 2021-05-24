package k.co.willynganga.codematatasessions.repository

import k.co.willynganga.codematatasessions.model.OAuthUser
import k.co.willynganga.codematatasessions.other.PROVIDER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class OAuth2UserRepositoryTest @Autowired constructor(
    private val underTest: OAuth2UserRepository
) {

    @Test
    fun `can find user with valid email`() {
        //given
        val email = "jane.doe@gmail.com"
        val oAuthUser = OAuthUser(
            "Jane Doe",
            "https://localhost:8080/img/1",
            email,
            true,
            PROVIDER.GOOGLE,
            "1234567890"
        )
        underTest.saveAndFlush(oAuthUser)

        //when
        val exists = underTest.findByEmail(email)

        //then
        assertThat(exists).isNotNull
    }

    @Test
    fun `cannot find user with invalid email`() {
        //given
        var email = "jane.doe@gmail.com"
        val oAuthUser = OAuthUser(
            "Jane Doe",
            "https://localhost:8080/img/1",
            email,
            true,
            PROVIDER.GOOGLE,
            "1234567890"
        )
        underTest.saveAndFlush(oAuthUser)
        email = "jane_doe@gmail.com"
        //when
        val exists = underTest.findByEmail(email)

        //then
        assertThat(exists).isNull()
    }

    @Test
    fun `can delete user with valid email`() {
        //given
        val email = "jane.doe@gmail.com"
        val oAuthUser = OAuthUser(
            "Jane Doe",
            "https://localhost:8080/img/1",
            email,
            true,
            PROVIDER.GOOGLE,
            "1234567890"
        )
        underTest.saveAndFlush(oAuthUser)

        //when
        val deleted = underTest.deleteByEmail(email)

        //then
        assertThat(deleted).isEqualTo(1)
    }

    @Test
    fun `cannot deleted user with invalid email`() {
        //given
        var email = "jane.doe@gmail.com"
        val oAuthUser = OAuthUser(
            "Jane Doe",
            "https://localhost:8080/img/1",
            email,
            true,
            PROVIDER.GOOGLE,
            "1234567890"
        )
        underTest.saveAndFlush(oAuthUser)
        email = "jane_doe@gmail.com"

        //when
        val deleted = underTest.deleteByEmail(email)

        //then
        assertThat(deleted).isEqualTo(0)
    }
}