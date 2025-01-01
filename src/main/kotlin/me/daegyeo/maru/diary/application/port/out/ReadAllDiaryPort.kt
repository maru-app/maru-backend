package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.Diary
import java.util.UUID

fun interface ReadAllDiaryPort {
    fun readAllDiaryByUserId(userId: UUID): List<Diary>
}
