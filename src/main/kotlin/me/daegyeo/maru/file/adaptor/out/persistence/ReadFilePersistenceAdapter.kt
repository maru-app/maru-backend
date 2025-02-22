package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.FileMapper
import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.port.out.ReadFilePort
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

    override fun readFileByPath(path: String): File? {
        val file = fileRepository.findByPath(path).getOrNull()
        return file?.let {
            fileMapper.toDomain(it)
        }
    }
}
