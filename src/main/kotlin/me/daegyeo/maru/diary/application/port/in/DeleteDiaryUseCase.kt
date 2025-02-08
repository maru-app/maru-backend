package me.daegyeo.maru.diary.application.port.`in`

import java.util.UUID

fun interface DeleteDiaryUseCase {
    fun deleteDiary(
        diaryId: Long,
        userId: UUID,
    ): Boolean
}
