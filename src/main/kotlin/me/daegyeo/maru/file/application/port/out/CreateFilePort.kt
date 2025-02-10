package me.daegyeo.maru.file.application.port.out

import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.application.port.out.dto.CreateFileDto

fun interface CreateFilePort {
    fun createFile(input: CreateFileDto): File
}
