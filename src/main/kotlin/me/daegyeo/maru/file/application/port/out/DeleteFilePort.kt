package me.daegyeo.maru.file.application.port.out

import me.daegyeo.maru.file.constant.FileStatus
import java.time.ZonedDateTime

interface DeleteFilePort {
    fun deleteFile(fileId: Long): Boolean

    fun deleteFileByStatusAndCreatedAtBefore(
        status: FileStatus,
        dateTime: ZonedDateTime,
    ): Boolean

    fun deleteUploadedOrOrphanedFile(): Boolean
}
