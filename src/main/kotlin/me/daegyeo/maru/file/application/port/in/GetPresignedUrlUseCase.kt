package me.daegyeo.maru.file.application.port.`in`

import java.util.UUID

interface GetPresignedUrlUseCase {
    fun getPresignedGetUrl(
        fileName: String,
        userId: UUID,
    ): String

    fun getPresignedPutUrl(
        originalFileName: String,
        userId: UUID,
    ): String
}
