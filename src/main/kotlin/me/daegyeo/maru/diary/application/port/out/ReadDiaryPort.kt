package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.DiaryWithUserId

fun interface ReadDiaryPort {
    fun readDiary(diaryId: Long): DiaryWithUserId?
}
