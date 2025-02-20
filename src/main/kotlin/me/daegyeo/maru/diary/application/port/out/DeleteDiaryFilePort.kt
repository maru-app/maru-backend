package me.daegyeo.maru.diary.application.port.out

fun interface DeleteDiaryFilePort {
    fun deleteDiaryFile(
        diaryId: Long,
        fileId: Long,
    ): Boolean
}
