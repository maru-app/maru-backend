package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.UpdateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.UpdateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.UpdateDiaryPort
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateDiaryService(
    private val updatedDiaryPort: UpdateDiaryPort,
    private val getDiaryUseCase: GetDiaryUseCase,
    private val updateFilePort: UpdateFilePort,
    private val encryptDiaryUseCase: EncryptDiaryUseCase,
    private val getImagePathInContentService: GetImagePathInContentService,
) : UpdateDiaryUseCase {
    @Transactional
    override fun updateDiary(
        diaryId: Long,
        userId: UUID,
        input: UpdateDiaryCommand,
    ): Boolean {
        val isExistsAndOwnedDiary = getDiaryUseCase.getDiaryByDiaryId(diaryId, userId)

        val imagePaths = getImagePathInContentService.getImagePathInContent(input.content)
        imagePaths.forEach {
            updateFilePort.updateFileStatus(it, FileStatus.USED)
        }

        val encryptedContent = encryptDiaryUseCase.encryptDiary(input.content)
        updatedDiaryPort.updateDiary(
            diaryId = isExistsAndOwnedDiary.diaryId,
            title = input.title,
            content = encryptedContent,
        )
        return true
    }
}
