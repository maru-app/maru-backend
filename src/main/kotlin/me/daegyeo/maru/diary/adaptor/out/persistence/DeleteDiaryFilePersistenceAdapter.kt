package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.application.port.out.DeleteDiaryFilePort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class DeleteDiaryFilePersistenceAdapter(private val diaryFileRepository: DiaryFileRepository) : DeleteDiaryFilePort {
    override fun deleteDiaryFile(
        diaryId: Long,
        fileId: Long,
    ): Boolean {
        val diaryFile = diaryFileRepository.findByDiaryIdAndFileId(diaryId, fileId).getOrNull()
        return diaryFile?.let {
            diaryFileRepository.delete(it)
            true
        } ?: false
    }
}
