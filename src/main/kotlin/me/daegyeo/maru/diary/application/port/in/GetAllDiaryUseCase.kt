package me.daegyeo.maru.diary.application.port.`in`

import me.daegyeo.maru.diary.application.domain.Diary
import java.util.UUID

fun interface GetAllDiaryUseCase {
    fun getAllDiaryByUserId(userId: UUID): List<Diary>
}
