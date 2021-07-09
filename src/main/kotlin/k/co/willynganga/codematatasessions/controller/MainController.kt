package k.co.willynganga.codematatasessions.controller

import k.co.willynganga.codematatasessions.model.*
import k.co.willynganga.codematatasessions.other.Constants.Companion.IMAGE_BASE_URL
import k.co.willynganga.codematatasessions.security.CurrentUser
import k.co.willynganga.codematatasessions.security.UserPrincipal
import k.co.willynganga.codematatasessions.service.*
import k.co.willynganga.codematatasessions.util.STATUS
import k.co.willynganga.codematatasessions.util.Utils.Companion.convertFileToBytes
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
class MainController(
    private val recordingService: RecordingService,
    private val oAuthUserService: OAuthUserService,
    private val imageService: ImageService,
    private val imageUrlService: ImageUrlService,
    private val eventService: EventService,
    private val eventImageUrlService: EventImageUrlService,
    private val instructorService: InstructorService
) {

    //Recording
    @GetMapping("/recording/all")
    fun getAllRecordings(@PageableDefault(value = 12, page = 0) pageable: Pageable): RecordingsResponse {
        return recordingService.findAllRecordings(pageable)
    }

    @PostMapping("/recording/add")
    fun addRecording(
        @RequestParam file: MultipartFile,
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam videoUrl: String,
        @RequestParam git: String,
        @RequestParam date: String,
        @RequestParam instructorUsername: String
    ): Response {
        val instructor = instructorService.findByUsername(instructorUsername)
        instructor?.let {
            val recording = Recording(title, description, videoUrl, git, date, instructor = it)
            val response = recordingService.addRecording(recording)
            val image = imageService.addImage(convertFileToBytes(file)!!)
            imageUrlService.addUrl(IMAGE_BASE_URL + image?.id, recording)
            return response
        }
        return Response(1, STATUS.FAIL, "No instructor matches the username $instructorUsername!")
    }

    @GetMapping("/recording/{id}")
    fun getRecordingById(@PathVariable id: Long): Recording? {
        return recordingService.findRecordingById(id)
    }

    @GetMapping("/recording/by-title")
    fun getRecordingByTitle(@RequestParam title: String): Recording? {
        return recordingService.findRecordingByTitle(title)
    }

    @GetMapping("/recording/by-date")
    fun getRecordingByDate(
        @PageableDefault(value = 12, page = 0) pageable: Pageable,
        @RequestParam("date") date: String
    ): RecordingsResponse {
        return recordingService.findRecordingByDate(pageable, date)
    }

    @GetMapping("/recording/by-title-and-date")
    fun getRecordingByTitleAndString(
        @PageableDefault(value = 12, page = 0) pageable: Pageable,
        @RequestParam("title") title: String,
        @RequestParam("date") date: String
    ): RecordingsResponse {
        return recordingService.findRecordingByTitleAndDate(pageable, title, date)
    }

    @DeleteMapping("/recording/delete/{id}")
    fun deleteRecordingWithId(@PathVariable id: Long): Response = recordingService.deleteRecordingById(id)

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

    //Event
    @GetMapping("/event/all")
    fun getAllEvents(@PageableDefault(value = 12, page = 0) pageable: Pageable): EventsResponse =
        eventService.getAllEvents(pageable)

    @PostMapping("/event/add")
    fun saveNewEvent(
        @RequestParam file: MultipartFile,
        @RequestParam title: String,
        @RequestParam description: String,
        @RequestParam startTime: String,
        @RequestParam endTime: String,
        @RequestParam meetUrl: String,
        @RequestParam prerequisites: String,
        @RequestParam username: String
    ): Response {
        val instructor = instructorService.findByUsername(username)
        instructor?.let {
            val event = Event(
                title,
                description,
                LocalDateTime.parse(startTime),
                LocalDateTime.parse(endTime),
                meetUrl,
                prerequisites,
                instructor = it
            )
            val response = eventService.saveEvent(event)
            val image = imageService.addImage(convertFileToBytes(file)!!)
            eventImageUrlService.addUrl(IMAGE_BASE_URL + image?.id, event)
            return response
        }
        return Response(1, STATUS.FAIL, "No instructor matches the username $username!")
    }

    @GetMapping("/event/{id}")
    fun getEventById(@PathVariable id: Long): Event? = eventService.getEvent(id)

    @DeleteMapping("/event/delete/{id}")
    fun deleteEventById(@PathVariable id: Long): Response = eventService.deleteEvent(id)

    //Instructor
    @GetMapping("/instructor/all")
    fun getAllInstructors(@PageableDefault(value = 12, page = 0) pageable: Pageable): InstructorsResponse {
        return instructorService.getAllInstructors(pageable)
    }

    @PostMapping("/instructor/add")
    fun addNewInstructor(
        @RequestParam username: String,
        @RequestParam name: String,
        @RequestParam email: String,
        @RequestParam phone: String,
        @RequestParam github: String?,
        @RequestParam twitter: String?,
        @RequestParam instagram: String?,
        @RequestParam bioInfo: String,
    ): Response {
        val instructor = Instructor(username, email, name, bioInfo, github!!, twitter!!, instagram!!, phone)
        return instructorService.addInstructor(instructor)
    }

    @DeleteMapping("/instructor/delete")
    fun deleteInstructor(@RequestParam username: String): Response {
        return instructorService.deleteInstructor(username)
    }

}