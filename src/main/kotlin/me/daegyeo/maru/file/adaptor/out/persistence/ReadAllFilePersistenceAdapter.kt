package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.FileMapper
import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.port.out.ReadAllFilePort
import me.daegyeo.maru.file.constant.FileStatus
import org.springframework.stereotype.Component

@Component
class ReadAllFilePersistenceAdapter(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) : ReadAllFilePort {
    override fun readAllFileByStatus(status: FileStatus): List<File> {
        val files = fileRepository.findByStatus(status)
        return files.map { fileMapper.toDomain(it) }
    }
}
