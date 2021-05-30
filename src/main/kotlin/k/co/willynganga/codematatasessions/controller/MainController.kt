package k.co.willynganga.codematatasessions.controller

import k.co.willynganga.codematatasessions.model.OAuthUser
import k.co.willynganga.codematatasessions.model.Recording
import k.co.willynganga.codematatasessions.model.Response
import k.co.willynganga.codematatasessions.security.CurrentUser
import k.co.willynganga.codematatasessions.security.UserPrincipal
import k.co.willynganga.codematatasessions.service.ImageService
import k.co.willynganga.codematatasessions.service.ImageUrlService
import k.co.willynganga.codematatasessions.service.OAuthUserService
import k.co.willynganga.codematatasessions.service.RecordingService
import k.co.willynganga.codematatasessions.util.Utils.Companion.convertFileToBytes
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
open class MainController(
    private val recordingService: RecordingService,
    private val oAuthUserService: OAuthUserService,
    private val imageService: ImageService,
    private val imageUrlService: ImageUrlService
) {

    //Recording
    @GetMapping("/recording/all")
    fun getAllRecordings(): List<Recording> {
        return recordingService.findAllRecordings()
    }

    @PostMapping("/recording/add")
    fun addRecording(
        @RequestParam file: MultipartFile,
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam videoUrl: String,
        @RequestParam date: String,
        @RequestParam instructor: String
    ): Response {
        val recording = Recording(title, description, videoUrl, date, instructor)
        val response = recordingService.addRecording(recording)
        val image = imageService.addImage(convertFileToBytes(file)!!)
        imageUrlService.addUrl("https://code-matata.herokuapp.com/api/v1/images/${image?.id}", recording)
        return response
    }

    @GetMapping("/recording/{id}")
    fun getRecordingById(@PathVariable id: Long): Recording? {
        return recordingService.findRecordingById(id)
    }

    @GetMapping("/recording/by-title")
    fun getRecordingByTitle(@RequestParam title: String): Recording? {
        return recordingService.findRecordingByTitle(title)
    }

    @GetMapping("/recording/by-instructor")
    fun getRecordingByInstructorUsername(@RequestParam username: String): List<Recording> {
        return recordingService.findRecordingByInstructorUsername(username)
    }

    @GetMapping("/recording/by-date")
    fun getRecordingByDate(@RequestParam("date") date: String): List<Recording> {
        return recordingService.findRecordingByDate(date)
    }

    @GetMapping("/recording/by-title-and-date")
    fun getRecordingByTitleAndString(
        @RequestParam("title") title: String,
        @RequestParam("date") date: String
    ): List<Recording> {
        return recordingService.findRecordingByTitleAndDate(title, date)
    }

    //OAuthUser

    @GetMapping("/oauth-user/all")
    fun getAllOAuthUsers(): List<OAuthUser> = oAuthUserService.findAllUsers()

    @GetMapping("/oauth-user/me")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): OAuthUser {
        return oAuthUserService.findById(userPrincipal.getId()).orElse(null)
    }

    //Image
    @GetMapping(path = ["/images/{id}"], produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImage(@PathVariable id: Long): ResponseEntity<ByteArray> {
        val image = imageService.findImageById(id)
        val imageBytes = image?.image
        return if (imageBytes != null)
            ResponseEntity(imageBytes, HttpStatus.OK)
        else ResponseEntity(HttpStatus.NOT_FOUND)
    }
}