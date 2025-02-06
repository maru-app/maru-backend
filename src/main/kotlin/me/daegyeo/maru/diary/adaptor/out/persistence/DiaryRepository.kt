package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.persistence.DiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface DiaryRepository : JpaRepository<DiaryEntity, Long> {
    @Query(
        """SELECT new me.daegyeo.maru.diary.application.domain.Diary(d.diaryId, d.title, '', d.createdAt, d.updatedAt) 
            FROM DiaryEntity d 
            WHERE d.userId = :userId""",
    )
    fun findByUserIdExcludingContent(userId: UUID): List<Diary>
}
