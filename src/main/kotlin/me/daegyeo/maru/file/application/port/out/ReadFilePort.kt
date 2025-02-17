package me.daegyeo.maru.file.application.port.out

import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.constant.FileStatus
import java.util.UUID

interface ReadFilePort {
    fun readFile(fileId: Long): File?

    fun readFileByPathAndUserId(
        path: String,
        userId: UUID,
    ): File?

    fun readAllFileByStatus(status: FileStatus): List<File>
}
