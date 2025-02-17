package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.FileMapper
import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.port.out.ReadFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class ReadFilePersistenceAdapter(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) : ReadFilePort {
    override fun readFile(fileId: Long): File? {
        val file = fileRepository.findById(fileId).getOrNull()
        return file?.let { fileMapper.toDomain(it) }
    }

    override fun readFileByPathAndUserId(
        path: String,
        userId: UUID,
    ): File? {
        val file = fileRepository.findByPathAndUserId(path, userId).getOrNull()
        return file?.let {
            fileMapper.toDomain(it)
        }
    }

    override fun readAllFileByStatus(status: FileStatus): List<File> {
        val files = fileRepository.findByStatus(status)
        return files.map { fileMapper.toDomain(it) }
    }
}
