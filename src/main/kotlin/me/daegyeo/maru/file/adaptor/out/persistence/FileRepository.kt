package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.file.application.persistence.FileEntity
import me.daegyeo.maru.file.constant.FileStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.ZonedDateTime
import java.util.Optional
import java.util.UUID

interface FileRepository : JpaRepository<FileEntity, Long> {
    fun findByPathAndUserId(
        path: String,
        userId: UUID,
    ): Optional<FileEntity>

    fun findByPath(path: String): Optional<FileEntity>

    fun deleteByStatusAndCreatedAtBefore(
        status: FileStatus,
        dateTime: ZonedDateTime,
    )

    fun findAllByStatusIn(status: Collection<FileStatus>): List<FileEntity>

    fun deleteByStatusIn(statuses: Collection<FileStatus>)
}
