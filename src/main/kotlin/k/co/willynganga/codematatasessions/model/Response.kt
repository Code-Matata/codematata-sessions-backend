package k.co.willynganga.codematatasessions.model

import k.co.willynganga.codematatasessions.util.STATUS

data class Response(
    val requestCode: Int,
    val status: STATUS,
    val message: String
)
