package me.daegyeo.maru.diary.application.domain

import java.time.ZonedDateTime

// userId is not included in the Diary domain. userId not open to the public.
data class Diary(
    val diaryId: Long,
    var title: String,
    var content: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val deletedAt: ZonedDateTime? = null,
)
