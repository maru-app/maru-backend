package me.daegyeo.maru.diary.application.port.`in`.command

import java.util.UUID

data class CreateDiaryCommand(
    val title: String,
    val content: String,
    val userId: UUID,
    val emoji: String,
)
