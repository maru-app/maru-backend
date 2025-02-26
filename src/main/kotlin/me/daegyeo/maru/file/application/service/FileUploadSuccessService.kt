package me.daegyeo.maru.file.application.service

import me.daegyeo.maru.file.application.error.FileError
import me.daegyeo.maru.file.application.port.`in`.FileUploadSuccessUseCase
import me.daegyeo.maru.file.application.port.out.ReadFilePort
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import me.daegyeo.maru.shared.exception.ServiceException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FileUploadSuccessService(
    private val readFilePort: ReadFilePort,
    private val updateFilePort: UpdateFilePort,
) : FileUploadSuccessUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun fileUploadSuccess(fileKey: String): Boolean {
        val path = fileKey.split("/").last()
        val file = readFilePort.readFileByPath(path) ?: throw ServiceException(FileError.FILE_NOT_FOUND)
        updateFilePort.updateFileStatus(file.fileId, FileStatus.UPLOADED)
        logger.info("File 데이터를 UPLAODED 상태로 변경했습니다. ${file.fileId}")
        return true
    }
}
