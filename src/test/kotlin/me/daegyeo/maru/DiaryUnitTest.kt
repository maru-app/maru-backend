package me.daegyeo.maru

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.EncryptDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.CreateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.CreateDiaryPort
import me.daegyeo.maru.diary.application.service.CreateDiaryService
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.time.ZonedDateTime
import java.util.UUID

@Suppress("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
class DiaryUnitTest {
    private val getUserUseCase = mock(GetUserUseCase::class.java)
    private val createDiaryPort = mock(CreateDiaryPort::class.java)
    private val encryptDiaryUseCase = mock(EncryptDiaryUseCase::class.java)
    private val createDiaryService = CreateDiaryService(getUserUseCase, createDiaryPort, encryptDiaryUseCase)

    @Test
    fun `일기를 성공적으로 가져옴`() {
    }

    @Test
    fun `본인 일기가 아니라면 가져올 때 오류를 반환함`() {
    }

    @Test
    fun `모든 일기를 성공적으로 가져옴`() {
    }

    @Test
    fun `일기를 성공적으로 생성함`() {
        val userId = UUID.randomUUID()
        val user =
            User(
                userId = userId,
                email = "",
                vendor = Vendor.GOOGLE,
                nickname = "",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            )
        val title = "FOO BAR"
        val content = "<p>Hello, World</p>"
        val encryptedContent = "ENCRYPTED_CONTENT"
        val diary =
            Diary(
                diaryId = 1,
                userId = userId,
                title = title,
                content = encryptedContent,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )

        `when`(getUserUseCase.getUser(userId)).thenReturn(user)
        `when`(encryptDiaryUseCase.encryptDiary(content)).thenReturn(encryptedContent)
        `when`(createDiaryPort.createDiary(any())).thenReturn(diary)

        val result = createDiaryService.createDiary(CreateDiaryCommand(title, content, userId))

        verify(getUserUseCase).getUser(userId)
        verify(encryptDiaryUseCase).encryptDiary(content)
        verify(createDiaryPort).createDiary(any())

        assert(result.userId == userId)
        assert(result.title == title)
    }

    @Test
    fun `일기를 성공적으로 수정함`() {
    }

    @Test
    fun `본인 일기가 아니라면 수정 시 오류를 반환함`() {
    }

    @Test
    fun `일기를 성공적으로 삭제함`() {
    }

    @Test
    fun `본인 일기가 아니라면 삭제 시 오류를 반환함`() {
    }
}
