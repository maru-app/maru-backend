package me.daegyeo.maru.diary.application.port.`in`

import me.daegyeo.maru.diary.application.port.`in`.command.AttachDiaryFileFromContentCommand

fun interface AttachDiaryFileFromContentUseCase {
    fun attachDiaryFileFromContent(input: AttachDiaryFileFromContentCommand)
}
