package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.error.DiaryError
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.out.ReadDiaryPort
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

class GetDiaryService(private val readDiaryPort: ReadDiaryPort) : GetDiaryUseCase {
    @Transactional(readOnly = true)
    override fun getDiaryByDiaryId(
        diaryId: Long,
        currentUserId: UUID,
    ): Diary {
        val result = readDiaryPort.readDiary(diaryId) ?: throw ServiceException(DiaryError.DIARY_NOT_FOUND)
        if (result.userId != currentUserId) {
            throw ServiceException(DiaryError.DIARY_IS_NOT_OWNED)
        }
        return Diary(
            diaryId = result.diaryId,
            title = result.title,
            content = result.content,
            createdAt = result.createdAt,
            updatedAt = result.updatedAt,
        )
    }
}
