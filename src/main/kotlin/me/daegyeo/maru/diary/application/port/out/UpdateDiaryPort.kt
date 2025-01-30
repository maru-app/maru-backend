package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.Diary

fun interface UpdateDiaryPort {
    fun updateDiary(
        diaryId: Long,
        title: String,
        content: String,
    ): Diary?
}
