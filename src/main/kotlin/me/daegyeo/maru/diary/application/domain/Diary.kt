package me.daegyeo.maru.diary.application.domain

import java.time.ZonedDateTime
import java.util.UUID

data class Diary(
    val diaryId: Long,
    var title: String,
    var content: String,
    val userId: UUID,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
