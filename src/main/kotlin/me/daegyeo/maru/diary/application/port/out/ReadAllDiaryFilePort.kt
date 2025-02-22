package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.DiaryFile

fun interface ReadAllDiaryFilePort {
    fun readAllDiaryFileByDiaryId(diaryId: Long): List<DiaryFile>
}
