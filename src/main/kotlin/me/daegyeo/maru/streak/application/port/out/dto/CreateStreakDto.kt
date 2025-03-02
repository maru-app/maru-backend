package me.daegyeo.maru.streak.application.port.out.dto

import java.util.UUID

data class CreateStreakDto(
    val userId: UUID,
    val streak: Int,
    val bestStreak: Int,
)
