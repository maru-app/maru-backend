package me.daegyeo.maru.file.application.service

import me.daegyeo.maru.file.application.error.FileError
import me.daegyeo.maru.file.application.port.`in`.FileUploadSuccessUseCase
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.stereotype.Service

@Service
class FileUploadSuccessService(
    private val updateFilePort: UpdateFilePort,
) : FileUploadSuccessUseCase {
    override fun fileUploadSuccess(fileKey: String): Boolean {
        val path = fileKey.split("/").last()
        updateFilePort.updateFileStatus(path, FileStatus.UPLOADED)
            ?: throw ServiceException(FileError.FILE_NOT_FOUND)
        return true
    }
}
