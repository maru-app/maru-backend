package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.error.DiaryError
import me.daegyeo.maru.diary.application.port.`in`.AttachDiaryFileFromContentUseCase
import me.daegyeo.maru.diary.application.port.`in`.CreateDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.AttachDiaryFileFromContentCommand
import me.daegyeo.maru.diary.application.port.`in`.command.CreateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.CreateDiaryPort
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryDto
import me.daegyeo.maru.diary.constant.DiaryValidation
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.streak.application.domain.event.CreatedStreakEvent
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateDiaryService(
    private val getUserUseCase: GetUserUseCase,
    private val createDiaryPort: CreateDiaryPort,
    private val encryptDiaryUseCase: EncryptDiaryUseCase,
    private val attachDiaryFileFromContentUseCase: AttachDiaryFileFromContentUseCase,
    private val applicationEventPublisher: ApplicationEventPublisher,
) :
    CreateDiaryUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun createDiary(input: CreateDiaryCommand): Diary {
        input.also {
            if (it.content.length > DiaryValidation.DIARY_CONTENT_MAX_LENGTH) {
                throw ServiceException(DiaryError.DIARY_LENGTH_EXCEEDED)
            }
            if (it.emoji.length > DiaryValidation.DIARY_EMOJI_MAX_LENGTH) {
                throw ServiceException(DiaryError.DIARY_LENGTH_EXCEEDED)
            }
        }

        val user = getUserUseCase.getUser(input.userId)

        val encryptedContent = encryptDiaryUseCase.encryptDiary(input.content)
        val diary =
            createDiaryPort.createDiary(
                CreateDiaryDto(
                    userId = user.userId,
                    title = input.title,
                    content = encryptedContent,
                    emoji = input.emoji,
                ),
            )

        attachDiaryFileFromContentUseCase.attachDiaryFileFromContent(
            AttachDiaryFileFromContentCommand(
                userId = user.userId,
                diaryId = diary.diaryId,
                content = input.content,
            ),
        )

        logger.info("Diary 데이터를 생성했습니다. ${diary.diaryId}")

        applicationEventPublisher.publishEvent(CreatedStreakEvent(user.userId))

        return diary.let {
            it.content = ""
            it
        }
    }
}
