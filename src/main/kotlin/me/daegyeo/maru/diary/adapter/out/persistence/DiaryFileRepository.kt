package me.daegyeo.maru.diary.adapter.out.persistence

import me.daegyeo.maru.diary.application.persistence.DiaryFileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface DiaryFileRepository : JpaRepository<DiaryFileEntity, Long> {
    fun findByDiaryIdAndFileId(
        diaryId: Long,
        fileId: Long,
    ): Optional<DiaryFileEntity>

    fun findAllByDiaryId(diaryId: Long): List<DiaryFileEntity>

    @Query(
        """
        SELECT diaryFile FROM DiaryFileEntity diaryFile
        JOIN FETCH diaryFile.file
        WHERE diaryFile.diaryId = :diaryId
    """,
    )
    fun findAllByDiaryIdWithFile(diaryId: Long): List<DiaryFileEntity>

    fun deleteAllByDiaryId(diaryId: Long)
}
