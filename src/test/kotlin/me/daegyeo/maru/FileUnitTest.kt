package me.daegyeo.maru

import io.minio.MinioClient
import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.error.FileError
import me.daegyeo.maru.file.application.port.out.CreateFilePort
import me.daegyeo.maru.file.application.port.out.ReadFilePort
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.application.port.out.dto.CreateFileDto
import me.daegyeo.maru.file.application.service.FileUploadSuccessService
import me.daegyeo.maru.file.application.service.GetPresignedUrlService
import me.daegyeo.maru.file.constant.FileStatus
import me.daegyeo.maru.shared.exception.ServiceException
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.time.ZonedDateTime
import java.util.UUID

@Suppress("NonAsciiCharacters")
@ExtendWith(MockitoExtension::class)
class FileUnitTest {
    private val minioClient = mock(MinioClient::class.java)
    private val createFilePort = mock(CreateFilePort::class.java)
    private val readFilePort = mock(ReadFilePort::class.java)
    private val updateFilePort = mock(UpdateFilePort::class.java)

    private val getPresignedUrlService = GetPresignedUrlService(minioClient, createFilePort, readFilePort)
    private val fileUploadSuccessService = FileUploadSuccessService(readFilePort, updateFilePort)

    @BeforeEach
    fun setup() {
        GetPresignedUrlService::class.java.getDeclaredField("bucket").apply {
            isAccessible = true
            set(getPresignedUrlService, "test-bucket")
        }
        GetPresignedUrlService::class.java.getDeclaredField("urlExpirySeconds").apply {
            isAccessible = true
            set(getPresignedUrlService, 3600)
        }
    }

    @Test
    fun `GET PresignedURL을 생성함`() {
        val userId = UUID.randomUUID()
        val file =
            File(
                fileId = 1,
                path = "test.jpg",
                originalPath = "test.jpg",
                userId = userId,
                status = FileStatus.UPLOADED,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            )

        `when`(readFilePort.readFileByPathAndUserId("test.jpg", userId)).thenReturn(file)
        `when`(minioClient.getPresignedObjectUrl(any())).thenReturn("http://acme.com/get/test.jpg")

        val presignedUrl = getPresignedUrlService.getPresignedGetUrl("test.jpg", userId)

        assert(presignedUrl.url == "http://acme.com/get/test.jpg")
        assert(presignedUrl.fileName == "test.jpg")
    }

    @Test
    fun `존재하지 않는 파일에 대한 GET PresignedURL을 생성하면 오류를 반환함`() {
        val userId = UUID.randomUUID()

        `when`(readFilePort.readFileByPathAndUserId("test.jpg", userId)).thenReturn(null)

        val exception =
            assertThrows(ServiceException::class.java) {
                getPresignedUrlService.getPresignedGetUrl("test.jpg", userId)
            }
        assert(exception.error == FileError.FILE_NOT_FOUND)
    }

    @Test
    fun `PUT PresignedURL을 생성함`() {
        val userId = UUID.randomUUID()

        `when`(minioClient.getPresignedObjectUrl(any())).thenReturn("http://acme.com/put/random-uuid.jpg")

        val presignedUrl = getPresignedUrlService.getPresignedPutUrl("original.jpg", userId)

        verify(createFilePort).createFile(
            CreateFileDto(
                userId = userId,
                path = presignedUrl.fileName,
                originalPath = "original.jpg",
                status = FileStatus.PENDING,
            ),
        )
        assert(presignedUrl.url == "http://acme.com/put/random-uuid.jpg")
    }

    @Test
    fun `Object Stroage에 파일이 업로드되면 성공 처리함`() {
        val fileId = 1L
        val file =
            File(
                fileId = fileId,
                path = "test.jpg",
                originalPath = "original_test.jpg",
                userId = UUID.randomUUID(),
                status = FileStatus.PENDING,
                createdAt = ZonedDateTime.now(),
                updatedAt = ZonedDateTime.now(),
                deletedAt = null,
            )

        `when`(readFilePort.readFileByPath("test.jpg")).thenReturn(file)

        val result = fileUploadSuccessService.fileUploadSuccess("test.jpg")

        verify(updateFilePort).updateFileStatus(fileId, FileStatus.UPLOADED)
        assert(result)
    }
}
