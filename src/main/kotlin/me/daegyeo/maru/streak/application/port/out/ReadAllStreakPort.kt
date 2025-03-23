package me.daegyeo.maru.streak.application.port.out

import me.daegyeo.maru.streak.application.domain.Streak
import me.daegyeo.maru.streak.application.domain.StreakGroupByDate
import me.daegyeo.maru.streak.application.domain.StreakRank
import java.util.UUID

interface ReadAllStreakPort {
    fun readAllStreakByUserId(userId: UUID): List<Streak>

    fun readAllStreakByUserIdAndYearGroupByDate(
        userId: UUID,
        year: Int,
    ): List<StreakGroupByDate>

    fun readAllStreakRankOrderByStreakDesc(year: Int): List<StreakRank>
}
