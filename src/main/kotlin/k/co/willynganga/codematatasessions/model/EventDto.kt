package k.co.willynganga.codematatasessions.model

import k.co.willynganga.codematatasessions.extensions.monthShortForm

class EventDto(event: Event) {
    val id = event.id
    val title = event.title
    val description = event.description
    val startTime = event.startTime
    val endTime = event.endTime
    val meetUrl = event.meetUrl
    val eventId = event.eventId
    val prerequisites = event.prerequisites
    val date = Date(event.startTime.dayOfMonth.toString(), event.startTime.monthShortForm())
    val imageUrl = event.imageUrl
    val instructor = event.instructor
}