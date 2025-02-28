package me.daegyeo.maru.streak.application.port.out

import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.port.out.dto.CreateStreakDto

fun interface CreateStreakPort {
    fun createStreak(input: CreateStreakDto): Streak
}
