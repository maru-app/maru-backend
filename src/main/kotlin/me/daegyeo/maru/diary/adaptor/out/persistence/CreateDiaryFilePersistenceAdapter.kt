package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.DiaryFileMapper
import me.daegyeo.maru.diary.application.domain.DiaryFile
import me.daegyeo.maru.diary.application.persistence.DiaryFileEntity
import me.daegyeo.maru.diary.application.port.out.CreateDiaryFilePort
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryFileDto
import org.springframework.stereotype.Component

@Component
class CreateDiaryFilePersistenceAdapter(
    private val diaryFileRepository: DiaryFileRepository,
    private val diaryFileMapper: DiaryFileMapper,
) : CreateDiaryFilePort {
    override fun createDiaryFile(input: CreateDiaryFileDto): DiaryFile {
        val diaryFile =
            DiaryFileEntity(
                diaryId = input.diaryId,
                fileId = input.fileId,
            )

        val saved = diaryFileRepository.save(diaryFile)

        return diaryFileMapper.toDomain(saved)
    }
}
