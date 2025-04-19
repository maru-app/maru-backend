package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.AttachDiaryFileFromContentUseCase
import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.UpdateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.AttachDiaryFileFromContentCommand
import me.daegyeo.maru.diary.application.port.`in`.command.UpdateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.DeleteDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.UpdateDiaryPort
import me.daegyeo.maru.diary.application.port.out.dto.UpdateDiaryDto
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateDiaryService(
    private val updatedDiaryPort: UpdateDiaryPort,
    private val getDiaryUseCase: GetDiaryUseCase,
    private val updateFilePort: UpdateFilePort,
    private val readAllDiaryFilePort: ReadAllDiaryFilePort,
    private val deleteDiaryFilePort: DeleteDiaryFilePort,
    private val encryptDiaryUseCase: EncryptDiaryUseCase,
    private val attachDiaryFileFromContentUseCase: AttachDiaryFileFromContentUseCase,
) : UpdateDiaryUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun updateDiary(
        diaryId: Long,
        userId: UUID,
        input: UpdateDiaryCommand,
    ): Boolean {
        val isExistsAndOwnedDiary = getDiaryUseCase.getDiaryByDiaryId(diaryId, userId)

        val existsDiaryFiles = readAllDiaryFilePort.readAllDiaryFileByDiaryId(diaryId)
        existsDiaryFiles.forEach {
            deleteDiaryFilePort.deleteDiaryFile(it.diaryId, it.fileId)
            updateFilePort.updateFileStatus(it.fileId, FileStatus.ORPHANED)
        }

        attachDiaryFileFromContentUseCase.attachDiaryFileFromContent(
            AttachDiaryFileFromContentCommand(
                userId = userId,
                diaryId = diaryId,
                content = input.content,
            ),
        )

        val encryptedContent = encryptDiaryUseCase.encryptDiary(input.content)
        updatedDiaryPort.updateDiary(
            diaryId = isExistsAndOwnedDiary.diaryId,
            input =
                UpdateDiaryDto(
                    title = input.title,
                    content = encryptedContent,
                    emoji = input.emoji,
                ),
        )

        logger.info("Diary 데이터를 수정했습니다. diaryId: $diaryId")

        return true
    }
}
