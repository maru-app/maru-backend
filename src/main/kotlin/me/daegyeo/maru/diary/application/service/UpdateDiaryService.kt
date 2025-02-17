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

        // TODO: 기존에 있던 이미지가 수정 과정 중에 삭제됐을 경우 해당 이미지의 상태를 변경하는 로직 필요
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
