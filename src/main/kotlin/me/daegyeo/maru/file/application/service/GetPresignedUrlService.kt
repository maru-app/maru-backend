package me.daegyeo.maru.file.application.service

import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.errors.MinioException
import io.minio.http.Method
import me.daegyeo.maru.file.application.domain.PresignedUrl
import me.daegyeo.maru.file.application.error.FileError
import me.daegyeo.maru.file.application.port.`in`.GetPresignedUrlUseCase
import me.daegyeo.maru.file.application.port.out.CreateFilePort
import me.daegyeo.maru.file.application.port.out.ReadFilePort
import me.daegyeo.maru.file.application.port.out.dto.CreateFileDto
import me.daegyeo.maru.file.constant.FileStatus
import me.daegyeo.maru.shared.error.CommonError
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import java.util.concurrent.TimeUnit

@Service
class GetPresignedUrlService(
    private val minioClient: MinioClient,
    private val createFilePort: CreateFilePort,
    private val readFilePort: ReadFilePort,
) : GetPresignedUrlUseCase {
    @Value("\${minio.bucket-name}")
    private lateinit var bucket: String

    @Value("\${minio.presigned-url-expiration}")
    private var urlExpirySeconds: Int = 0

    @Transactional(readOnly = true)
    override fun getPresignedGetUrl(
        fileName: String,
        userId: UUID,
    ): PresignedUrl {
        val fileDomain =
            readFilePort.readFileByPathAndUserId(fileName, userId)
                ?: throw ServiceException(FileError.FILE_NOT_FOUND)

        val presignedUrl = generatePresignedUrl(fileDomain.path, Method.GET)

        return PresignedUrl(
            url = presignedUrl,
            fileName = fileDomain.path,
        )
    }

    @Transactional
    override fun getPresignedPutUrl(
        originalFileName: String,
        userId: UUID,
    ): PresignedUrl {
        val extension = originalFileName.substringAfterLast(".")
        val objectName = UUID.randomUUID().toString()
        val fileName = "$objectName.$extension"
        val presignedUrl = generatePresignedUrl(fileName, Method.PUT)

        createFilePort.createFile(
            CreateFileDto(
                userId = userId,
                path = fileName,
                originalPath = originalFileName,
                status = FileStatus.PENDING,
            ),
        )

        return PresignedUrl(
            url = presignedUrl,
            fileName = fileName,
        )
    }

    private fun generatePresignedUrl(
        fileName: String,
        method: Method,
    ): String {
        try {
            val args =
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .`object`(fileName)
                    .method(method)
                    .expiry(urlExpirySeconds, TimeUnit.SECONDS)
                    .build()

            return minioClient.getPresignedObjectUrl(args)
        } catch (e: MinioException) {
            throw ServiceException(CommonError.INTERNAL_SERVER_ERROR)
        }
    }
}
