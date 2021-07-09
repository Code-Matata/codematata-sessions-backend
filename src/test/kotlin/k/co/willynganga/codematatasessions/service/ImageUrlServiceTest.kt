package k.co.willynganga.codematatasessions.service

import k.co.willynganga.codematatasessions.model.ImageUrl
import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.repository.ImageUrlRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.given
import org.mockito.kotlin.verify
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ImageUrlServiceTest {

    @Mock
    private lateinit var imageUrlRepository: ImageUrlRepository
    private lateinit var underTest: ImageUrlService

    @BeforeEach
    internal fun setUp() {
        underTest = ImageUrlService(imageUrlRepository)
    }

    @Test
    fun `can add url`() {
        //given
        val recording = Recording(
            "Spring Boot 101",
            "This is an introductory course on Spring Boot framework.",
            "https://youtube.com",
            "github.com/janedoe/spring-boot.git",
            "2021-05-27",
        )
        val url = "http://codematata/api/v1/image/1"
        //when
        underTest.addUrl(url, recording)

        //then
        val argumentCaptor = argumentCaptor<ImageUrl>()
        verify(imageUrlRepository).saveAndFlush(argumentCaptor.capture())
        val capturedImageUrl = argumentCaptor.firstValue

        assertEquals(capturedImageUrl.url, url)
        assertEquals(capturedImageUrl.recording, recording)
    }

    @Test
    fun `can get url with id of image url`() {
        //given
        val recording = Recording(
            "Spring Boot 101",
            "This is an introductory course on Spring Boot framework.",
            "https://youtube.com",
            "github.com/janedoe/spring-boot.git",
            "2021-05-27",
        )
        val url = "http://codematata/api/v1/image/1"
        val id: Long = 0
        given(imageUrlRepository.findById(id))
            .willReturn(
                Optional.of(ImageUrl(url, recording))
            )
        //when
        underTest.getUrl(id)

        //then
        val argumentCaptor = argumentCaptor<Long>()
        verify(imageUrlRepository).findById(argumentCaptor.capture())
        val capturedId = argumentCaptor.firstValue

        assertEquals(capturedId, id)
    }

    @Test
    fun deleteUrl() {
        //given
        val id: Long = 0

        //when
        underTest.deleteUrl(id)

        //then
        val argumentCaptor = argumentCaptor<Long>()
        verify(imageUrlRepository).deleteById(argumentCaptor.capture())
        val capturedId = argumentCaptor.firstValue

        assertEquals(capturedId, id)
    }
}