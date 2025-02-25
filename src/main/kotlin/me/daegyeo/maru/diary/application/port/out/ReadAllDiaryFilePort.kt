package me.daegyeo.maru.diary.application.port.out

import me.daegyeo.maru.diary.application.domain.DiaryFile
import me.daegyeo.maru.diary.application.domain.DiaryFileWithFile

interface ReadAllDiaryFilePort {
    fun readAllDiaryFileByDiaryId(diaryId: Long): List<DiaryFile>

    fun readAllDiaryFileByDiaryIdWithFile(diaryId: Long): List<DiaryFileWithFile>
}
