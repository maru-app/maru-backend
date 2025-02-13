package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.FileMapper
import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.port.out.UpdateFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class UpdateFilePersistenceAdapter(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) : UpdateFilePort {
    override fun updateFileStatus(
        path: String,
        status: FileStatus,
    ): File? {
        val file = fileRepository.findByPath(path).getOrNull()
        file?.let {
            it.status = status
            return fileMapper.toDomain(fileRepository.save(it))
        }
        return null
    }
}
