package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.application.persistence.DiaryEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DiaryRepository : JpaRepository<DiaryEntity, Long> {
    fun findByUserId(userId: UUID): List<DiaryEntity>
}
