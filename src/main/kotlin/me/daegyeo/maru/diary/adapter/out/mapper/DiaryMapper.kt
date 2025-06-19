package me.daegyeo.maru.diary.adapter.out.mapper

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.domain.DiaryWithUserId
import me.daegyeo.maru.diary.application.persistence.DiaryEntity
import org.springframework.stereotype.Component

@Component
class DiaryMapper {
    fun toDomain(diaryEntity: DiaryEntity): Diary {
        return Diary(
            diaryId = diaryEntity.diaryId!!,
            title = diaryEntity.title,
            content = diaryEntity.content,
            emoji = diaryEntity.emoji,
            createdAt = diaryEntity.createdAt!!,
            updatedAt = diaryEntity.updatedAt!!,
            deletedAt = diaryEntity.deletedAt,
        )
    }

    fun toDomainWithUserId(diaryEntity: DiaryEntity): DiaryWithUserId {
        return DiaryWithUserId(
            diaryId = diaryEntity.diaryId!!,
            userId = diaryEntity.userId,
            title = diaryEntity.title,
            content = diaryEntity.content,
            emoji = diaryEntity.emoji,
            createdAt = diaryEntity.createdAt!!,
            updatedAt = diaryEntity.updatedAt!!,
            deletedAt = diaryEntity.deletedAt,
        )
    }
}
