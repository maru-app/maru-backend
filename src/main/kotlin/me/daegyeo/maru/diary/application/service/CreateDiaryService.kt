package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.CreateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetImagePathInContentUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.CreateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.CreateDiaryPort
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryDto
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateDiaryService(
    private val getUserUseCase: GetUserUseCase,
    private val createDiaryPort: CreateDiaryPort,
    private val updateFilePort: UpdateFilePort,
    private val encryptDiaryUseCase: EncryptDiaryUseCase,
    private val getImagePathInContentUseCase: GetImagePathInContentUseCase,
) :
    CreateDiaryUseCase {
    @Transactional
    override fun createDiary(input: CreateDiaryCommand): Diary {
        val user = getUserUseCase.getUser(input.userId)

        val imagePaths = getImagePathInContentUseCase.getImagePathInContent(input.content)
        imagePaths.forEach {
            updateFilePort.updateFileStatus(it, FileStatus.USED)
        }

        val encryptedContent = encryptDiaryUseCase.encryptDiary(input.content)
        val diary =
            createDiaryPort.createDiary(
                CreateDiaryDto(
                    userId = user.userId,
                    title = input.title,
                    content = encryptedContent,
                ),
            )
        return diary.let {
            it.content = ""
            it
        }
    }
}
