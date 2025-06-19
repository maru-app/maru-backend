package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.dto.UpdateDiaryDto

fun interface UpdateDiaryPort {
    fun updateDiary(
        diaryId: Long,
        input: UpdateDiaryDto,
    ): Diary?
}
