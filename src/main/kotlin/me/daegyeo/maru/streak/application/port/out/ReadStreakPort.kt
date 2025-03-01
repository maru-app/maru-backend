package me.daegyeo.maru.streak.application.port.out

import me.daegyeo.maru.streak.application.domain.Streak
import java.time.ZonedDateTime
import java.util.UUID

interface ReadStreakPort {
    fun readLatestStreakByUserId(userId: UUID): Streak?

    fun readLatestStreakByUserIdAndCreatedAt(
        userId: UUID,
        createdAt: ZonedDateTime,
    ): Streak?
}
