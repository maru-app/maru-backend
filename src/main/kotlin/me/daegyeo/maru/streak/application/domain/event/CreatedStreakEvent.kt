package me.daegyeo.maru.streak.application.domain.event

import java.util.UUID

data class CreatedStreakEvent(
    val userId: UUID,
)
