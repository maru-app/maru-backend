package me.daegyeo.maru.file.application.port.`in`

import io.minio.http.Method

fun interface GetPresignedUrlUseCase {
    fun getPresignedUrl(
        originalFileName: String,
        method: Method,
    ): String
}
