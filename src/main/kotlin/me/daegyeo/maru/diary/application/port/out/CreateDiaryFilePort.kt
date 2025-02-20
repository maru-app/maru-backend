package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.DiaryFile
import me.daegyeo.maru.diary.application.port.out.dto.CreateDiaryFileDto

fun interface CreateDiaryFilePort {
    fun createDiaryFile(input: CreateDiaryFileDto): DiaryFile
}
