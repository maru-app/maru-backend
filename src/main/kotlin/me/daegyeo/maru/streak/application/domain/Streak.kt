package me.daegyeo.maru.streak.application.domain

import java.time.ZonedDateTime
import java.util.UUID

data class Streak(
    val streak: Int,
    val bestStreak: Int,
    val userId: UUID,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
