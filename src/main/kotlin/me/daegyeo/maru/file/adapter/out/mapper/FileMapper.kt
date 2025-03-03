package me.daegyeo.maru.diary.adapter.out.mapper

import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.persistence.FileEntity
import org.springframework.stereotype.Component

@Component
class FileMapper {
    fun toDomain(fileEntity: FileEntity): File {
        return File(
            fileId = fileEntity.fileId!!,
            path = fileEntity.path,
            originalPath = fileEntity.originalPath,
            status = fileEntity.status,
            userId = fileEntity.userId,
            createdAt = fileEntity.createdAt!!,
            updatedAt = fileEntity.updatedAt!!,
            deletedAt = fileEntity.deletedAt,
        )
    }
}
