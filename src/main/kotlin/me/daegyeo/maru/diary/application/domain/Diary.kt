package me.daegyeo.maru.diary.application.domain

import java.time.ZonedDateTime
import java.util.UUID

data class Diary(
    val diaryId: Long,
    val userId: UUID,
    var content: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
