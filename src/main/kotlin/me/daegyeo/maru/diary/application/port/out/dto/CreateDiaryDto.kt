package me.daegyeo.maru.diary.application.port.out.dto

import java.util.UUID

data class CreateDiaryDto(
    val userId: UUID,
    val title: String,
    val emoji: String,
    val content: String,
)
