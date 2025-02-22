package me.daegyeo.maru.diary.application.domain

import java.time.ZonedDateTime

data class DiaryFile(
    val diaryFileId: Long,
    val diaryId: Long,
    val fileId: Long,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
