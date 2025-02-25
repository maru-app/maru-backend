package me.daegyeo.maru.diary.application.service

import io.minio.MinioClient
import io.minio.RemoveObjectsArgs
import io.minio.messages.DeleteObject
import me.daegyeo.maru.diary.application.port.`in`.DeleteDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.out.DeleteDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.DeleteDiaryPort
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryFilePort
import me.daegyeo.maru.file.application.port.out.DeleteFilePort
import me.daegyeo.maru.shared.error.CommonError
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteDiaryService(
    private val deleteDiaryPort: DeleteDiaryPort,
    private val getDiaryUseCase: GetDiaryUseCase,
    private val readAllDiaryFilePort: ReadAllDiaryFilePort,
    private val deleteDiaryFilePort: DeleteDiaryFilePort,
    private val deleteFilePort: DeleteFilePort,
    private val minioClient: MinioClient,
) :
    DeleteDiaryUseCase {
    @Value("\${minio.bucket-name}")
    private lateinit var bucket: String

    @Transactional
    override fun deleteDiary(
        diaryId: Long,
        userId: UUID,
    ): Boolean {
        val diary = getDiaryUseCase.getDiaryByDiaryId(diaryId, userId)
        return diary.let {
            val deletedFiles = readAllDiaryFilePort.readAllDiaryFileByDiaryIdWithFile(it.diaryId)
            val deletedObjects = deletedFiles.map { diaryFile -> DeleteObject(diaryFile.file.path) }

            val deletedObjectResult =
                minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                        .bucket(bucket)
                        .objects(deletedObjects)
                        .build(),
                )
            deletedObjectResult.forEach { result ->
                try {
                    val error = result.get()
                    if (error != null) {
                        // TODO: log error
                        throw ServiceException(CommonError.INTERNAL_SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    throw ServiceException(CommonError.INTERNAL_SERVER_ERROR)
                }
            }

            deleteDiaryFilePort.deleteAllByDiaryId(it.diaryId)
            deletedFiles.forEach {
                deleteFilePort.deleteFile(it.fileId)
            }
            deleteDiaryPort.deleteDiary(it.diaryId)
        }
    }
}
