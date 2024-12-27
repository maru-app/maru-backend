package me.daegyeo.maru.shared.util

import java.time.Instant
import java.util.Date

object DateFormat {
    fun parseDurationToDate(duration: String): Date {
        val currentTime = Instant.now().toEpochMilli()

        return when {
            duration.endsWith("d") -> {
                val days = duration.substringBefore("d").toIntOrNull() ?: 0
                Date(currentTime + days * 24 * 60 * 60 * 1000)
            }
            duration.endsWith("m") -> {
                val minutes = duration.substringBefore("m").toIntOrNull() ?: 0
                Date(currentTime + minutes * 60 * 1000)
            }
            duration.endsWith("w") -> {
                val weeks = duration.substringBefore("w").toIntOrNull() ?: 0
                Date(currentTime + weeks * 7 * 24 * 60 * 60 * 1000)
            }
            else -> Date(currentTime)
        }
    }
}
