package me.daegyeo.maru.diary.adapter.out.persistence

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.persistence.DiaryEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface DiaryRepository : JpaRepository<DiaryEntity, Long> {
    @Query(
        """SELECT new me.daegyeo.maru.diary.application.domain.Diary(d.diaryId, d.title, '', d.createdAt, d.updatedAt, d.deletedAt) 
            FROM DiaryEntity d 
            WHERE d.userId = :userId
            ORDER BY d.createdAt DESC""",
    )
    fun findAllByUserIdExcludingContent(userId: UUID): List<Diary>

    @Query(
        """SELECT new me.daegyeo.maru.diary.application.domain.Diary(d.diaryId, d.title, '', d.createdAt, d.updatedAt, d.deletedAt) 
            FROM DiaryEntity d 
            WHERE d.userId = :userId""",
    )
    fun findAllByUserIdExcludingContentWithPagination(
        userId: UUID,
        pageable: Pageable,
    ): Page<Diary>
}
