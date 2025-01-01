package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.DiaryMapper
import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.UpdateDiaryPort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class UpdateDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
    private val diaryMapper: DiaryMapper,
) : UpdateDiaryPort {
    override fun updateDiary(
        diaryId: Long,
        content: String,
    ): Diary? {
        val diary = diaryRepository.findById(diaryId).getOrNull()
        return diary?.let {
            it.content = content
            diaryMapper.toDomain(diaryRepository.save(it))
        }
    }
}
