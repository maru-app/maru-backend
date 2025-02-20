package me.daegyeo.maru.diary.adaptor.out.mapper

import me.daegyeo.maru.diary.application.domain.DiaryFile
import me.daegyeo.maru.diary.application.persistence.DiaryFileEntity
import org.springframework.stereotype.Component

@Component
class DiaryFileMapper {
    fun toDomain(diaryFileEntity: DiaryFileEntity): DiaryFile {
        return DiaryFile(
            diaryFileId = diaryFileEntity.diaryFileId!!,
            diaryId = diaryFileEntity.diaryId,
            fileId = diaryFileEntity.fileId,
            createdAt = diaryFileEntity.createdAt!!,
            updatedAt = diaryFileEntity.updatedAt!!,
        )
    }
}
