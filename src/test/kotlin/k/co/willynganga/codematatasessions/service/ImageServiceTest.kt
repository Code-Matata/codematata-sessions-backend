package k.co.willynganga.codematatasessions.service

import io.github.wickie73.mockito4kotlin.annotation.KMockitoAnnotations
import k.co.willynganga.codematatasessions.model.Image
import k.co.willynganga.codematatasessions.repository.ImageRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class ImageServiceTest {

    @Mock
    private lateinit var imageRepository: ImageRepository
    private lateinit var underTest: ImageService

    @BeforeEach
    internal fun setUp() {
        KMockitoAnnotations.openMocks(this)
        underTest = ImageService(imageRepository)
        imageRepository.deleteAll()
    }

    @Test
    fun `can find image with id`() {
        //given
        val id: Long = 0

        //when
        underTest.findImageById(id)

        //then
        val argumentCaptor = argumentCaptor<Long>()
        verify(imageRepository).findById(argumentCaptor.capture())
        val capturedId = argumentCaptor.firstValue

        assertEquals(capturedId, id)
    }

    @Test
    fun `can add an image`() {
        //given
        val bytes = ByteArray(100)

        //when
        underTest.addImage(bytes)

        //then
        val argumentCaptor = argumentCaptor<Image>()
        verify(imageRepository).saveAndFlush(argumentCaptor.capture())
        val capturedImage = argumentCaptor.firstValue

        assertEquals(capturedImage.image, bytes)
    }

    @Test
    fun `can delete image with valid id`() {
        //given
        val image = Image(ByteArray(100))
        val id: Long = 0
        given(imageRepository.findById(id)).willReturn(
            Optional.of(image)
        )
        //when
        underTest.deleteImage(id)

        //then
        val argumentCaptor = argumentCaptor<Long>()
        val argumentImageCaptor = argumentCaptor<Image>()
        verify(imageRepository).findById(argumentCaptor.capture())
        verify(imageRepository).delete(argumentImageCaptor.capture())
        val capturedId = argumentCaptor.firstValue
        val capturedImage = argumentImageCaptor.firstValue

        assertEquals(capturedId, id)
        assertEquals(capturedImage, image)
    }

    @Test
    fun `cannot delete image with invalid id`() {
        //given
        val id: Long = 0

        //when
        underTest.deleteImage(id)

        //then
        val argumentCaptor = argumentCaptor<Long>()
        verify(imageRepository).findById(argumentCaptor.capture())
        val capturedId = argumentCaptor.firstValue

        assertEquals(capturedId, id)
        verify(imageRepository, never()).delete(any())
    }
}