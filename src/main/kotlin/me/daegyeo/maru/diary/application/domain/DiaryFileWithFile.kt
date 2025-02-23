package me.daegyeo.maru.diary.application.domain

import me.daegyeo.maru.file.application.domain.File
import java.time.ZonedDateTime

data class DiaryFileWithFile(
    val diaryFileId: Long,
    val diaryId: Long,
    val fileId: Long,
    val file: File,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
