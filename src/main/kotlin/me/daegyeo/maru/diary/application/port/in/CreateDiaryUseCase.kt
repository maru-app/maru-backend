package me.daegyeo.maru.diary.application.port.`in`

import me.daegyeo.maru.diary.application.domain.Diary
import me.daegyeo.maru.diary.application.port.`in`.command.CreateDiaryCommand

fun interface CreateDiaryUseCase {
    fun createDiary(input: CreateDiaryCommand): Diary
}
