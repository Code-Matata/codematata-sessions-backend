package k.co.willynganga.codematatasessions.model

data class EventsResponse(
    val pages: Int,
    val currentPage: Int,
    val events: List<EventDto>
)
