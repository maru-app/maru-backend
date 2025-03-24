package me.daegyeo.maru.streak.application.port.`in`

import me.daegyeo.maru.streak.application.domain.StreakRank
import org.springframework.data.domain.Page

fun interface GetStreakRankingUseCase {
    fun getRanking(
        year: Int,
        page: Int,
        size: Int,
    ): Page<StreakRank>
}
