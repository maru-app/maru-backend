package me.daegyeo.maru.diary.adaptor.out.mapper

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.persistence.DiaryEntity
import org.springframework.stereotype.Component

@Component
class DiaryMapper {
    fun toDomain(diaryEntity: DiaryEntity): Diary {
        return Diary(
            diaryId = diaryEntity.diaryId!!,
            title = diaryEntity.title,
            content = diaryEntity.content,
            userId = diaryEntity.userId,
            createdAt = diaryEntity.createdAt!!,
            updatedAt = diaryEntity.updatedAt!!,
        )
    }
}
