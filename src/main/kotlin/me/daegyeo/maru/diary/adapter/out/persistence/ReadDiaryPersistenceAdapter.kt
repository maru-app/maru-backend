package me.daegyeo.maru.diary.adapter.out.persistence

import me.daegyeo.maru.diary.adapter.out.mapper.DiaryMapper
import me.daegyeo.maru.diary.application.domain.DiaryWithUserId
import me.daegyeo.maru.diary.application.port.out.ReadDiaryPort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class ReadDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
    private val diaryMapper: DiaryMapper,
) : ReadDiaryPort {
    override fun readDiary(diaryId: Long): DiaryWithUserId? {
        val diary = diaryRepository.findById(diaryId).getOrNull()
        return diary?.let { diaryMapper.toDomainWithUserId(it) }
    }
}
