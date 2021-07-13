package k.co.willynganga.codematatasessions.extensions

import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

fun LocalDateTime.monthShortForm(): String {
    val locale = Locale("en", "KE")
    return this.month.getDisplayName(TextStyle.SHORT, locale)
}