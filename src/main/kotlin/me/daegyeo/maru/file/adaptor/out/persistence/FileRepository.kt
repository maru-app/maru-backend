package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.file.application.persistence.FileEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface FileRepository : JpaRepository<FileEntity, Long> {
    fun findByPathAndUserId(
        path: String,
        userId: UUID,
    ): Optional<FileEntity>
}
