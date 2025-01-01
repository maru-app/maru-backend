package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryDto

fun interface CreateDiaryPort {
    fun createDiary(input: CreateDiaryDto): Diary
}
