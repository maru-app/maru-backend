package me.daegyeo.maru.streak.application.port.`in`

import me.daegyeo.maru.streak.application.port.`in`.result.GetStreakResult
import java.time.ZonedDateTime
import java.util.UUID

fun interface GetStreakUseCase {
    fun getStreak(
        userId: UUID,
        date: ZonedDateTime,
    ): GetStreakResult
}
