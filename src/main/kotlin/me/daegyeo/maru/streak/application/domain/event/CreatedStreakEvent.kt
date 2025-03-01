package me.daegyeo.maru.streak.application.domain.event

import java.util.UUID

data class CreatedStreakEvent(
    private val userId: UUID,
)
