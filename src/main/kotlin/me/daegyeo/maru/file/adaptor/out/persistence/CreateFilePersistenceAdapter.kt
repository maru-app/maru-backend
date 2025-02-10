package me.daegyeo.maru.file.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.FileMapper
import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.persistence.FileEntity
import me.daegyeo.maru.file.application.port.out.CreateFilePort
import me.daegyeo.maru.file.application.port.out.dto.CreateFileDto
import org.springframework.stereotype.Component

@Component
class CreateFilePersistenceAdapter(
    private val fileRepository: FileRepository,
    private val fileMapper: FileMapper,
) : CreateFilePort {
    override fun createFile(input: CreateFileDto): File {
        val file =
            FileEntity(
                userId = input.userId,
                path = input.path,
                originalPath = input.originalPath,
            )

        val savedFile = fileRepository.save(file)

        return fileMapper.toDomain(savedFile)
    }
}
