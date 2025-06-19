package me.daegyeo.maru

import io.minio.MinioClient
import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.domain.DiaryFile
import me.daegyeo.maru.diary.application.domain.DiaryWithUserId
import me.daegyeo.maru.diary.application.error.DiaryError
import me.daegyeo.maru.diary.application.port.`in`.*
import me.daegyeo.maru.diary.application.port.`in`.command.AttachDiaryFileFromContentCommand
import me.daegyeo.maru.diary.application.port.`in`.command.CreateDiaryCommand
import me.daegyeo.maru.diary.application.port.`in`.command.UpdateDiaryCommand
import me.daegyeo.maru.diary.application.port.out.*
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryFileDto
import me.daegyeo.maru.diary.application.port.out.dto.UpdateDiaryDto
import me.daegyeo.maru.diary.application.service.*
import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.port.out.DeleteFilePort
import me.daegyeo.maru.file.application.port.out.ReadFilePort
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.springframework.context.ApplicationEventPublisher
import java.time.ZonedDateTime
import java.util.UUID

@Suppress("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
class DiaryUnitTest {
    private val minioClient = mock(MinioClient::class.java)

    private val getUserUseCase = mock(GetUserUseCase::class.java)
    private val createDiaryPort = mock(CreateDiaryPort::class.java)
    private val readAllDiaryPort = mock(ReadAllDiaryPort::class.java)
    private val readDiaryPort = mock(ReadDiaryPort::class.java)
    private val updateDiaryPort = mock(UpdateDiaryPort::class.java)
    private val deleteDiaryPort = mock(DeleteDiaryPort::class.java)
    private val readFilePort = mock(ReadFilePort::class.java)
    private val updateFilePort = mock(UpdateFilePort::class.java)
    private val deleteFilePort = mock(DeleteFilePort::class.java)
    private val createDiaryFilePort = mock(CreateDiaryFilePort::class.java)
    private val readAllDiaryFilePort = mock(ReadAllDiaryFilePort::class.java)
    private val deleteDiaryFilePort = mock(DeleteDiaryFilePort::class.java)

    private val encryptDiaryUseCase = mock(EncryptDiaryUseCase::class.java)
    private val decryptDiaryUseCase = mock(DecryptDiaryUseCase::class.java)
    private val getDiaryUseCase = mock(GetDiaryUseCase::class.java)
    private val getImagePathInContentUseCase = mock(GetImagePathInContentUseCase::class.java)
    private val attachDiaryFileFromContentUseCase = mock(AttachDiaryFileFromContentUseCase::class.java)

    private val applicationEventPublisher = mock(ApplicationEventPublisher::class.java)

    private val createDiaryService =
        CreateDiaryService(
            getUserUseCase,
            createDiaryPort,
            encryptDiaryUseCase,
            attachDiaryFileFromContentUseCase,
            applicationEventPublisher,
        )
    private val getAllDiaryService = GetAllDiaryService(readAllDiaryPort)
    private val getDiaryService = GetDiaryService(readDiaryPort, decryptDiaryUseCase)
    private val updateDiaryService =
        UpdateDiaryService(
            updateDiaryPort,
            getDiaryUseCase,
            updateFilePort,
            readAllDiaryFilePort,
            deleteDiaryFilePort,
            encryptDiaryUseCase,
            attachDiaryFileFromContentUseCase,
        )
    private val deleteDiaryService =
        DeleteDiaryService(
            deleteDiaryPort,
            getDiaryUseCase,
            readAllDiaryFilePort,
            deleteDiaryFilePort,
            deleteFilePort,
            minioClient,
        )
    private val getImagePathInContentService = GetImagePathInContentService()
    private val attachDiaryFileFromContentService =
        AttachDiaryFileFromContentService(
            readFilePort,
            updateFilePort,
            createDiaryFilePort,
            getImagePathInContentUseCase,
        )

    @BeforeEach
    fun setup() {
        DeleteDiaryService::class.java.getDeclaredField("bucket").apply {
            isAccessible = true
            set(deleteDiaryService, "test-bucket")
        }
    }

    @Test
    fun `일기를 성공적으로 가져옴`() {
        val diaryId = 1L
        val userId = UUID.randomUUID()
        val diary =
            DiaryWithUserId(
                diaryId = diaryId,
                title = "FOO",
                userId = userId,
                content = "ENCRYPTED_CONTENT",
                emoji = "😊",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )

        `when`(readDiaryPort.readDiary(diaryId)).thenReturn(diary)
        `when`(decryptDiaryUseCase.decryptDiary(diary.content)).thenReturn("DECRYPTED_CONTENT")

        val result = getDiaryService.getDiaryByDiaryId(diaryId, userId)

        verify(readDiaryPort).readDiary(diaryId)
        assert(result.diaryId == diaryId)
    }

    @Test
    fun `본인 일기가 아니라면 가져올 때 오류를 반환함`() {
        val diaryId = 1L
        val userId = UUID.randomUUID()
        val diary =
            DiaryWithUserId(
                diaryId = diaryId,
                title = "FOO",
                userId = UUID.randomUUID(),
                content = "ENCRYPTED_CONTENT",
                emoji = "😊",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )

        `when`(readDiaryPort.readDiary(diaryId)).thenReturn(diary)

        val exception =
            assertThrows(ServiceException::class.java) {
                getDiaryService.getDiaryByDiaryId(diaryId, userId)
            }
        assert(exception.error == DiaryError.DIARY_IS_NOT_OWNED)
    }

    @Test
    fun `일기 내용이 공백인 상태로 모든 일기를 성공적으로 가져옴`() {
        val userId = UUID.randomUUID()

        `when`(readAllDiaryPort.readAllDiaryByUserId(userId)).thenReturn(
            listOf(
                Diary(
                    diaryId = 1,
                    title = "FOO",
                    content = "",
                    emoji = "😊",
                    createdAt = ZonedDateTime.now(),
                    updatedAt = ZonedDateTime.now(),
                ),
                Diary(
                    diaryId = 2,
                    title = "BAR",
                    content = "",
                    emoji = "😊",
                    createdAt = ZonedDateTime.now(),
                    updatedAt = ZonedDateTime.now(),
                ),
            ),
        )

        val result = getAllDiaryService.getAllDiaryByUserId(userId)

        verify(readAllDiaryPort).readAllDiaryByUserId(userId)
        assert(result.map { it.content }.all { it.isEmpty() })
        assert(result.size == 2)
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
                isPublicRanking = false,
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
                title = title,
                content = encryptedContent,
                emoji = "😊",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        val filePath = "foobar.png"
        val file =
            File(
                fileId = 1,
                userId = userId,
                path = filePath,
                originalPath = "original_foobar.png",
                status = FileStatus.UPLOADED,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            )

        `when`(getUserUseCase.getUser(userId)).thenReturn(user)
        `when`(encryptDiaryUseCase.encryptDiary(content)).thenReturn(encryptedContent)
        `when`(createDiaryPort.createDiary(any())).thenReturn(diary)
        `when`(getImagePathInContentUseCase.getImagePathInContent(content)).thenReturn(listOf(filePath))
        `when`(readFilePort.readFileByPathAndUserId(filePath, userId)).thenReturn(file)

        val result = createDiaryService.createDiary(CreateDiaryCommand(title, content, userId, "😊"))

        verify(getUserUseCase).getUser(userId)
        verify(encryptDiaryUseCase).encryptDiary(content)
        verify(createDiaryPort).createDiary(any())
        assert(result.title == title)
    }

    @Test
    fun `일기를 성공적으로 수정하고 기존에 첨부된 이미지를 정리함`() {
        val userId = UUID.randomUUID()
        val diaryId = 1L
        val title = "FOO BAR"
        val updateContent = "<p>Hello, World</p>"
        val encryptedContent = "ENCRYPTED_CONTENT"
        val diary =
            Diary(
                diaryId = diaryId,
                title = "PREV TITLE",
                content = "PREV_ENCRYPTED_CONTENT",
                emoji = "😊",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        val existsFile =
            DiaryFile(
                diaryFileId = 1,
                diaryId = diaryId,
                fileId = 1,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )
        val newFilePath = "new_foobar.png"
        val newFile =
            File(
                fileId = 2,
                userId = userId,
                path = newFilePath,
                originalPath = "new_original_foobar.png",
                status = FileStatus.UPLOADED,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            )

        `when`(getDiaryUseCase.getDiaryByDiaryId(diaryId, userId)).thenReturn(diary)
        `when`(encryptDiaryUseCase.encryptDiary(updateContent)).thenReturn(encryptedContent)
        `when`(readAllDiaryFilePort.readAllDiaryFileByDiaryId(diaryId)).thenReturn(listOf(existsFile))
        `when`(getImagePathInContentUseCase.getImagePathInContent(updateContent)).thenReturn(listOf(newFilePath))
        `when`(readFilePort.readFileByPathAndUserId(newFilePath, userId)).thenReturn(newFile)

        val result = updateDiaryService.updateDiary(diaryId, userId, UpdateDiaryCommand(title, updateContent, "😊"))

        verify(getDiaryUseCase).getDiaryByDiaryId(diaryId, userId)
        verify(encryptDiaryUseCase).encryptDiary(updateContent)
        verify(updateDiaryPort).updateDiary(diaryId, UpdateDiaryDto(title, encryptedContent, "😊"))
        verify(deleteDiaryFilePort).deleteDiaryFile(existsFile.diaryId, existsFile.fileId)
        verify(updateFilePort).updateFileStatus(existsFile.fileId, FileStatus.ORPHANED)

        assert(result)
    }

    @Test
    fun `본인 일기가 아니라면 수정 시 오류를 반환함`() {
        val userId = UUID.randomUUID()
        val diaryId = 1L
        val title = "FOO BAR"
        val updateContent = "<p>Hello, World</p>"

        `when`(
            getDiaryUseCase.getDiaryByDiaryId(
                diaryId,
                userId,
            ),
        ).thenThrow(ServiceException(DiaryError.DIARY_IS_NOT_OWNED))

        val exception =
            assertThrows(ServiceException::class.java) {
                updateDiaryService.updateDiary(diaryId, userId, UpdateDiaryCommand(title, updateContent, "😊"))
            }
        assert(exception.error == DiaryError.DIARY_IS_NOT_OWNED)
    }

    @Test
    fun `일기를 성공적으로 삭제함`() {
        val userId = UUID.randomUUID()
        val diaryId = 1L
        val diary =
            Diary(
                diaryId = diaryId,
                title = "FOO",
                content = "ENCRYPTED_CONTENT",
                emoji = "😊",
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
            )

        `when`(getDiaryUseCase.getDiaryByDiaryId(diaryId, userId)).thenReturn(diary)
        `when`(deleteDiaryPort.deleteDiary(diaryId)).thenReturn(true)

        val result = deleteDiaryService.deleteDiary(diaryId, userId)

        verify(getDiaryUseCase).getDiaryByDiaryId(diaryId, userId)
        verify(deleteDiaryPort).deleteDiary(diaryId)
        verify(deleteDiaryFilePort).deleteAllByDiaryId(diaryId)
        verify(deleteDiaryPort).deleteDiary(diaryId)
        assert(result)
    }

    @Test
    fun `본인 일기가 아니라면 삭제 시 오류를 반환함`() {
        val userId = UUID.randomUUID()
        val diaryId = 1L

        `when`(
            getDiaryUseCase.getDiaryByDiaryId(
                diaryId,
                userId,
            ),
        ).thenThrow(ServiceException(DiaryError.DIARY_IS_NOT_OWNED))

        val exception =
            assertThrows(ServiceException::class.java) {
                deleteDiaryService.deleteDiary(diaryId, userId)
            }
        assert(exception.error == DiaryError.DIARY_IS_NOT_OWNED)
    }

    @Test
    fun `일기 내용에서 첨부된 이미지 링크만 가져옴`() {
        val content = "<p>Hello, World</p>[image|foobar.png]<p>GoodBye</p>"
        val result = getImagePathInContentService.getImagePathInContent(content)
        assert(result == listOf("foobar.png"))
    }

    @Test
    fun `일기 내용에서 이미지 링크를 가져와 DiaryFile에 연결함`() {
        val userId = UUID.randomUUID()
        val diaryId = 1L
        val content = "<p>Hello, World</p>[image|foobar.png]<p>GoodBye</p>"
        val filePath = "foobar.png"
        val file =
            File(
                fileId = 1,
                userId = userId,
                path = filePath,
                originalPath = "original_foobar.png",
                status = FileStatus.UPLOADED,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            )

        `when`(getImagePathInContentUseCase.getImagePathInContent(content)).thenReturn(listOf(filePath))
        `when`(readFilePort.readFileByPathAndUserId(filePath, userId)).thenReturn(file)

        attachDiaryFileFromContentService.attachDiaryFileFromContent(
            AttachDiaryFileFromContentCommand(userId, diaryId, content),
        )

        verify(readFilePort).readFileByPathAndUserId(filePath, userId)
        verify(updateFilePort).updateFileStatus(file.fileId, FileStatus.USED)
        verify(createDiaryFilePort).createDiaryFile(CreateDiaryFileDto(diaryId, file.fileId))
    }
}
