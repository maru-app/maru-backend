package me.daegyeo.maru.file.application.domain

import java.time.ZonedDateTime
import java.util.UUID

data class File(
    val fileId: Long,
    val path: String,
    val originalPath: String,
    val userId: UUID,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
