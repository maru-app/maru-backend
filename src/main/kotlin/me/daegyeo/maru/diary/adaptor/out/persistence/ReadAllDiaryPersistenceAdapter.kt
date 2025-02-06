package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ReadAllDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
) : ReadAllDiaryPort {
    override fun readAllDiaryByUserId(userId: UUID): List<Diary> {
        val diaries = diaryRepository.findByUserIdExcludingContent(userId)
        return diaries
    }
}
