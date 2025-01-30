package me.daegyeo.maru.diary.application.domain

import java.time.ZonedDateTime

data class Diary(
    val diaryId: Long,
    var title: String,
    var content: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
