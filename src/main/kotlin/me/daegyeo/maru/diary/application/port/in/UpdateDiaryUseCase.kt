package me.daegyeo.maru.diary.application.port.`in`

import me.daegyeo.maru.diary.application.port.`in`.command.UpdateDiaryCommand
import java.util.UUID

fun interface UpdateDiaryUseCase {
    fun updateDiary(
        diaryId: Long,
        userId: UUID,
        input: UpdateDiaryCommand,
    ): Boolean
}
