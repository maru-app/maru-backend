package me.daegyeo.maru.streak.application.port.`in`

import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import java.util.UUID

fun interface GetAllStreakUseCase {
    fun getAllStreak(
        userId: UUID,
        year: Int,
    ): List<StreakGroupByDate>
}
