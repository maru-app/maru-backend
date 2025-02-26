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
import org.slf4j.LoggerFactory
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
) : DeleteDiaryUseCase {
    @Value("\${minio.bucket-name}")
    private lateinit var bucket: String

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun deleteDiary(
        diaryId: Long,
        userId: UUID,
    ): Boolean {
        val diary = getDiaryUseCase.getDiaryByDiaryId(diaryId, userId)
        diary.let {
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
                        logger.error("Diary 데이터와 연결된 File 데이터 삭제 중 오류가 발생했습니다. ${error.message()}")
                        throw ServiceException(CommonError.INTERNAL_SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    logger.error("Diary 데이터와 연결된 File 데이터 삭제 중 오류가 발생했습니다.")
                    e.printStackTrace()
                    throw ServiceException(CommonError.INTERNAL_SERVER_ERROR)
                }
            }

            deleteDiaryFilePort.deleteAllByDiaryId(it.diaryId)
            deletedFiles.forEach {
                deleteFilePort.deleteFile(it.fileId)
            }
            deleteDiaryPort.deleteDiary(it.diaryId)

            logger.info("Diary 데이터와 관련 파일을 삭제했습니다. ${it.diaryId}")
        }
        return true
    }
}
