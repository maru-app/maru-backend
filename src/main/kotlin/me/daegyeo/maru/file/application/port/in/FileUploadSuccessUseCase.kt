package me.daegyeo.maru.file.application.port.`in`

fun interface FileUploadSuccessUseCase {
    fun fileUploadSuccess(fileKey: String): Boolean
}
