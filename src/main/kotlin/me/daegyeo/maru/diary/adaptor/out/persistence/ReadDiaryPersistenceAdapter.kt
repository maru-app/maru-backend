package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.DiaryMapper
import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.ReadDiaryPort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class ReadDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
    private val diaryMapper: DiaryMapper,
) : ReadDiaryPort {
    override fun readDiary(diaryId: Long): Diary? {
        val diary = diaryRepository.findById(diaryId).getOrNull()
        return diary?.let { diaryMapper.toDomain(it) }
    }
}
