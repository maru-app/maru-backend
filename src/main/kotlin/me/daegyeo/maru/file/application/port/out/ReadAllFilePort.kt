package me.daegyeo.maru.file.application.port.out

import me.daegyeo.maru.file.application.domain.File
import me.daegyeo.maru.file.constant.FileStatus

fun interface ReadAllFilePort {
    fun readAllFileByStatus(status: FileStatus): List<File>
}
