package me.daegyeo.maru.streak.application.domain

import java.util.UUID

data class StreakGroupByDate(
    val userId: UUID,
    val date: String,
    val count: Long,
)
