package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.error.DiaryError
import me.daegyeo.maru.diary.application.port.`in`.DecryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.out.ReadDiaryPort
import me.daegyeo.maru.shared.exception.ServiceException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetDiaryService(private val readDiaryPort: ReadDiaryPort, private val decryptDiaryUseCase: DecryptDiaryUseCase) :
    GetDiaryUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun getDiaryByDiaryId(
        diaryId: Long,
        currentUserId: UUID,
    ): Diary {
        val result = readDiaryPort.readDiary(diaryId) ?: throw ServiceException(DiaryError.DIARY_NOT_FOUND)
        if (result.userId != currentUserId) {
            throw ServiceException(DiaryError.DIARY_IS_NOT_OWNED)
        }
        val decryptedContent = decryptDiaryUseCase.decryptDiary(result.content)
        logger.info("Diary 데이터를 조회했습니다. diaryId: $diaryId")
        return Diary(
            diaryId = result.diaryId,
            title = result.title,
            content = decryptedContent,
            emoji = result.emoji,
            createdAt = result.createdAt,
            updatedAt = result.updatedAt,
        )
    }
}
