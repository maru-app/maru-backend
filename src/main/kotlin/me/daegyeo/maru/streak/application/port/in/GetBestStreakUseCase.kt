package me.daegyeo.maru.streak.application.port.`in`

import java.time.ZonedDateTime
import java.util.UUID

fun interface GetBestStreakUseCase {
    fun getBestStreakByDate(
        userId: UUID,
        date: ZonedDateTime,
    ): Int
}
