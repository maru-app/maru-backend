package me.daegyeo.maru.diary.adapter.out.persistence

import me.daegyeo.maru.diary.adapter.out.mapper.DiaryMapper
import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.persistence.DiaryEntity
import me.daegyeo.maru.diary.application.port.out.CreateDiaryPort
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryDto
import org.springframework.stereotype.Component

@Component
class CreateDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
    private val diaryMapper: DiaryMapper,
) : CreateDiaryPort {
    override fun createDiary(input: CreateDiaryDto): Diary {
        val diary =
            DiaryEntity(
                userId = input.userId,
                title = input.title,
                content = input.content,
            )

        val savedDiary = diaryRepository.save(diary)

        return diaryMapper.toDomain(savedDiary)
    }
}
