package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.adaptor.out.mapper.DiaryFileMapper
import me.daegyeo.maru.diary.application.domain.DiaryFile
import me.daegyeo.maru.diary.application.domain.DiaryFileWithFile
import me.daegyeo.maru.diary.application.port.out.ReadAllDiaryFilePort
import org.springframework.stereotype.Component

@Component
class ReadAllDiaryFilePersistenceAdapter(
    private val diaryFileRepository: DiaryFileRepository,
    private val diaryFileMapper: DiaryFileMapper,
) : ReadAllDiaryFilePort {
    override fun readAllDiaryFileByDiaryId(diaryId: Long): List<DiaryFile> {
        val diaryFiles = diaryFileRepository.findAllByDiaryId(diaryId)
        return diaryFiles.map { diaryFileMapper.toDomain(it) }
    }

    override fun readAllDiaryFileByDiaryIdWithFile(diaryId: Long): List<DiaryFileWithFile> {
        val diaryFiles = diaryFileRepository.findAllByDiaryIdWithFile(diaryId)
        return diaryFiles.map { diaryFileMapper.toDomainWithFile(it) }
    }
}
