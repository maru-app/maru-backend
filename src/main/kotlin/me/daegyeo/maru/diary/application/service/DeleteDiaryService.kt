package me.daegyeo.maru.diary.application.service

import io.minio.MinioClient
import io.minio.RemoveObjectsArgs
import io.minio.messages.DeleteObject
import me.daegyeo.maru.diary.application.port.`in`.DeleteDiaryUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetDiaryUseCase
import me.daegyeo.maru.diary.application.port.out.DeleteDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.DeleteDiaryPort
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryFilePort
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
            val deletedFiles =
                readAllDiaryFilePort.readAllDiaryFileByDiaryIdWithFile(it.diaryId)
                    .map { diaryFile -> DeleteObject(diaryFile.file.path) }

            minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                    .bucket(bucket)
                    .objects(deletedFiles)
                    .build(),
            )

            deleteDiaryFilePort.deleteAllByDiaryId(it.diaryId)
            deleteDiaryPort.deleteDiary(it.diaryId)
        }
    }
}
