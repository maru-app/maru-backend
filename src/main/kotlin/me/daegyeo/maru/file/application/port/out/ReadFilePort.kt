package me.daegyeo.maru.file.application.port.out

import me.daegyeo.maru.file.application.domain.File

fun interface ReadFilePort {
    fun readFile(fileId: Long): File?
}
