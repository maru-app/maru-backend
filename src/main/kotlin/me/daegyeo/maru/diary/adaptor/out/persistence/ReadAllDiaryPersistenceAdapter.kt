package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.DiaryMapper
import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ReadAllDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
    private val diaryMapper: DiaryMapper,
) : ReadAllDiaryPort {
    override fun readAllDiaryByUserId(userId: UUID): List<Diary> {
        val diaries = diaryRepository.findByUserId(userId)
        return diaries.map { diaryMapper.toDomain(it) }
    }
}
