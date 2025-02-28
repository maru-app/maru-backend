package me.daegyeo.maru.streak.application.port.out

import me.daegyeo.maru.streak.application.domain.Streak
import java.util.UUID

fun interface ReadAllStreakPort {
    fun readAllStreakByUserId(userId: UUID): List<Streak>
}
