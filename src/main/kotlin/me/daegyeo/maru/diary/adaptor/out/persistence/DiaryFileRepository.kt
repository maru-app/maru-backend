package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.application.persistence.DiaryFileEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface DiaryFileRepository : JpaRepository<DiaryFileEntity, Long> {
    fun findByDiaryIdAndFileId(
        diaryId: Long,
        fileId: Long,
    ): Optional<DiaryFileEntity>

    fun findAllByDiaryId(diaryId: Long): List<DiaryFileEntity>

    fun deleteAllByDiaryId(diaryId: Long)
}
