package me.daegyeo.maru.diary.application.port.`in`.command

import java.util.UUID

data class AttachDiaryFileFromContentCommand(
    val userId: UUID,
    val diaryId: Long,
    val content: String,
)
