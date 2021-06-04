package k.co.willynganga.codematatasessions.model

data class RecordingsResponse(
    val pages: Int,
    val currentPage: Int,
    val recordings: List<Recording>
)
