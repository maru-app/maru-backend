package me.daegyeo.maru.file.application.port.`in`

import me.daegyeo.maru.file.application.domain.PresignedUrl
import java.util.UUID

interface GetPresignedUrlUseCase {
    fun getPresignedGetUrl(
        fileName: String,
        userId: UUID,
    ): PresignedUrl

    fun getPresignedPutUrl(
        originalFileName: String,
        userId: UUID,
    ): PresignedUrl
}
