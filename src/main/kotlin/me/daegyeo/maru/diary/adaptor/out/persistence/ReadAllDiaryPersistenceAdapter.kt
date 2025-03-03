package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryPort
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ReadAllDiaryPersistenceAdapter(
    private val diaryRepository: DiaryRepository,
) : ReadAllDiaryPort {
    override fun readAllDiaryByUserId(userId: UUID): List<Diary> {
        return diaryRepository.findAllByUserIdExcludingContent(userId)
    }

    override fun readAllDiaryByUserIdWithPagination(
        userId: UUID,
        page: Int,
        size: Int,
    ): Page<Diary> {
        return diaryRepository.findAllByUserIdExcludingContentWithPagination(
            userId,
            PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt"))),
        )
    }
}
