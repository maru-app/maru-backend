package me.daegyeo.maru.file.application.domain

import me.daegyeo.maru.file.constant.FileStatus
import java.time.ZonedDateTime
import java.util.UUID

data class File(
    val fileId: Long,
    val path: String,
    val originalPath: String,
    var status: FileStatus,
    val userId: UUID,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    val deletedAt: ZonedDateTime? = null,
)
