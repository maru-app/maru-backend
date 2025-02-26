package me.daegyeo.maru.diary.application.service

import me.daegyeo.maru.diary.application.port.`in`.AttachDiaryFileFromContentUseCase
import me.daegyeo.maru.diary.application.port.`in`.GetImagePathInContentUseCase
import me.daegyeo.maru.diary.application.port.`in`.command.AttachDiaryFileFromContentCommand
import me.daegyeo.maru.diary.application.port.out.CreateDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryFileDto
import me.daegyeo.maru.file.application.port.out.ReadFilePort
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AttachDiaryFileFromContentService(
    private val readFilePort: ReadFilePort,
    private val updateFilePort: UpdateFilePort,
    private val createDiaryFilePort: CreateDiaryFilePort,
    private val getImagePathInContentUseCase: GetImagePathInContentUseCase,
) : AttachDiaryFileFromContentUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun attachDiaryFileFromContent(input: AttachDiaryFileFromContentCommand) {
        val imagePaths = getImagePathInContentUseCase.getImagePathInContent(input.content)
        imagePaths.forEach {
            val file = readFilePort.readFileByPathAndUserId(it, input.userId) ?: return@forEach
            updateFilePort.updateFileStatus(file.fileId, FileStatus.USED)
            createDiaryFilePort.createDiaryFile(
                CreateDiaryFileDto(
                    diaryId = input.diaryId,
                    fileId = file.fileId,
                ),
            )
        }

        if (imagePaths.isNotEmpty()) {
            logger.info("DiaryFile 데이터를 생성했습니다. diaryId: ${input.diaryId}")
        }
    }
}
