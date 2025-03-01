package me.daegyeo.maru.streak.application.port.`in`

import me.daegyeo.maru.streak.application.domain.Streak
import java.util.UUID

fun interface AddTodayStreakUseCase {
    fun addTodayStreak(userId: UUID): Streak
}
