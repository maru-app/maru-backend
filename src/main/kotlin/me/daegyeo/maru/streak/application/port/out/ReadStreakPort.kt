package me.daegyeo.maru.streak.application.port.out

import me.daegyeo.maru.streak.application.domain.Streak
import java.util.UUID

fun interface ReadStreakPort {
    fun readLatestStreakByUserId(userId: UUID): Streak?
}
