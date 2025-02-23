package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.AttachDiaryFileFromContentUseCase
import me.daegyeo.maru.diary.application.port.`in`.CreateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.AttachDiaryFileFromContentCommand
import me.daegyeo.maru.diary.application.port.`in`.command.CreateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.CreateDiaryPort
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryDto
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateDiaryService(
    private val getUserUseCase: GetUserUseCase,
    private val createDiaryPort: CreateDiaryPort,
    private val encryptDiaryUseCase: EncryptDiaryUseCase,
    private val attachDiaryFileFromContentUseCase: AttachDiaryFileFromContentUseCase,
) :
    CreateDiaryUseCase {
    @Transactional
    override fun createDiary(input: CreateDiaryCommand): Diary {
        val user = getUserUseCase.getUser(input.userId)

        val encryptedContent = encryptDiaryUseCase.encryptDiary(input.content)
        val diary =
            createDiaryPort.createDiary(
                CreateDiaryDto(
                    userId = user.userId,
                    title = input.title,
                    content = encryptedContent,
                ),
            )

        attachDiaryFileFromContentUseCase.attachDiaryFileFromContent(
            AttachDiaryFileFromContentCommand(
                userId = user.userId,
                diaryId = diary.diaryId,
                content = input.content,
            ),
        )

        return diary.let {
            it.content = ""
            it
        }
    }
}
