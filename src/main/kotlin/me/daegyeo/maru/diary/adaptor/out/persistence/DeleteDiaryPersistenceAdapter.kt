package me.daegyeo.maru.diary.adaptor.out.persistence

import me.daegyeo.maru.diary.application.port.out.DeleteDiaryPort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class DeleteDiaryPersistenceAdapter(private val diaryRepository: DiaryRepository) : DeleteDiaryPort {
    override fun deleteDiary(diaryId: Long): Boolean {
        val diary = diaryRepository.findById(diaryId).getOrNull()
        return diary?.let {
            diaryRepository.delete(it)
            true
        } ?: false
    }
}
