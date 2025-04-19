package me.daegyeo.maru.diary.adapter.out.persistence

import me.daegyeo.maru.diary.adapter.out.mapper.DiaryMapper
import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.UpdateDiaryPort
import me.daegyeo.maru.diary.application.port.out.dto.UpdateDiaryDto
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class UpdateDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
    private val diaryMapper: DiaryMapper,
) : UpdateDiaryPort {
    override fun updateDiary(
        diaryId: Long,
        input: UpdateDiaryDto,
    ): Diary? {
        val diary = diaryRepository.findById(diaryId).getOrNull()
        return diary?.let {
            it.title = input.title
            it.content = input.content
            it.emoji = input.emoji
            diaryMapper.toDomain(diaryRepository.save(it))
        }
    }
}
