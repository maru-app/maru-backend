package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.DeleteDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.out.DeleteDiaryPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteDiaryService(private val deleteDiaryPort: DeleteDiaryPort, private val getDiaryUseCase: GetDiaryUseCase) :
    DeleteDiaryUseCase {
    @Transactional
    override fun deleteDiary(
        diaryId: Long,
        userId: UUID,
    ): Boolean {
        val diary = getDiaryUseCase.getDiaryByDiaryId(diaryId, userId)
        return diary.let {
            deleteDiaryPort.deleteDiary(it.diaryId)
        }
    }
}
