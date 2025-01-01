package me.daegyeo.maru.diary.application.port.out

fun interface DeleteDiaryPort {
    fun deleteDiary(diaryId: Long): Boolean
}
