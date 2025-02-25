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
    override fun readAllFileByStatusIn(status: Collection<FileStatus>): List<File> {
        val files = fileRepository.findAllByStatusIn(status)
        return files.map { fileMapper.toDomain(it) }
    }
}
