package me.daegyeo.maru.streak.application.port.`in`

import me.daegyeo.maru.streak.application.domain.StreakRank

fun interface GetStreakRankingUseCase {
    fun getRanking(year: Int): List<StreakRank>
}
