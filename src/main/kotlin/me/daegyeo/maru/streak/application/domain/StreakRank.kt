package me.daegyeo.maru.streak.application.domain

data class StreakRank(
    val rank: Long,
    val isPublicRanking: Boolean,
    val nickname: String,
    val streak: Int,
    val bestStreak: Int,
)
