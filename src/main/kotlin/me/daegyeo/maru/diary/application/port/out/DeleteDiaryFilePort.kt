package me.daegyeo.maru.diary.application.port.out

interface DeleteDiaryFilePort {
    fun deleteDiaryFile(
        diaryId: Long,
        fileId: Long,
    ): Boolean

    fun deleteAllByDiaryId(diaryId: Long): Boolean
}
