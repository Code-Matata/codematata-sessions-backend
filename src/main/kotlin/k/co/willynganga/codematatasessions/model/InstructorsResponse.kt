package k.co.willynganga.codematatasessions.model

data class InstructorsResponse(
    val pages: Int,
    val currentPage: Int,
    val instructors: List<Instructor>
)
