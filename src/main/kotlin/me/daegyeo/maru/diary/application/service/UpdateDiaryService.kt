package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetImagePathInContentUseCase
import me.daegyeo.maru.diary.application.port.`in`.UpdateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.UpdateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.CreateDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.DeleteDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.UpdateDiaryPort
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryFileDto
import me.daegyeo.maru.file.application.port.out.ReadFilePort
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateDiaryService(
    private val updatedDiaryPort: UpdateDiaryPort,
    private val getDiaryUseCase: GetDiaryUseCase,
    private val readFilePort: ReadFilePort,
    private val updateFilePort: UpdateFilePort,
    private val readAllDiaryFilePort: ReadAllDiaryFilePort,
    private val createDiaryFilePort: CreateDiaryFilePort,
    private val deleteDiaryFilePort: DeleteDiaryFilePort,
    private val encryptDiaryUseCase: EncryptDiaryUseCase,
    private val getImagePathInContentUseCase: GetImagePathInContentUseCase,
) : UpdateDiaryUseCase {
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

        val imagePaths = getImagePathInContentUseCase.getImagePathInContent(input.content)
        imagePaths.forEach {
            val file = readFilePort.readFileByPathAndUserId(it, userId) ?: return@forEach
            updateFilePort.updateFileStatus(file.fileId, FileStatus.USED)
            createDiaryFilePort.createDiaryFile(
                CreateDiaryFileDto(
                    diaryId = diaryId,
                    fileId = file.fileId,
                ),
            )
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
