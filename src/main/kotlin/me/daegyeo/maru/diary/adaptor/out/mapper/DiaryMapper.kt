package me.daegyeo.maru.diary.adaptor.out.mapper

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.persistence.DiaryEntity
import org.springframework.stereotype.Component

@Component
class DiaryMapper {
    fun toDomain(diaryEntity: DiaryEntity): Diary {
        return Diary(
            diaryId = diaryEntity.diaryId!!,
            userId = diaryEntity.user?.userId!!,
            content = diaryEntity.content,
            createdAt = diaryEntity.createdAt!!,
            updatedAt = diaryEntity.updatedAt!!,
        )
    }
}
