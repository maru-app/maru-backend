package me.daegyeo.maru.file.application.port.out

fun interface DeleteFilePort {
    fun deleteFile(fileId: Long): Boolean
}
