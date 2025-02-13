package me.daegyeo.maru.file.adaptor.`in`.web

import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.file.adaptor.`in`.web.dto.FileWebhookDto
import me.daegyeo.maru.file.adaptor.`in`.web.dto.GetDownloadPresignedUrlDto
import me.daegyeo.maru.file.adaptor.`in`.web.dto.GetUploadPresignedUrlDto
import me.daegyeo.maru.file.application.domain.PresignedUrl
import me.daegyeo.maru.file.application.port.`in`.FileUploadSuccessUseCase
import me.daegyeo.maru.file.application.port.`in`.GetPresignedUrlUseCase
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/file")
class FileController(
    private val getPresignedUrlUseCase: GetPresignedUrlUseCase,
    private val fileUploadSuccessUseCase: FileUploadSuccessUseCase,
) {
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/upload")
    fun getUploadPresignedUrl(
        @RequestBody body: GetUploadPresignedUrlDto,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): PresignedUrl {
        return getPresignedUrlUseCase.getPresignedPutUrl(body.originalFileName, auth.userId)
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/download")
    fun getDownloadPresignedUrl(
        @RequestBody body: GetDownloadPresignedUrlDto,
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): PresignedUrl {
        return getPresignedUrlUseCase.getPresignedGetUrl(
            body.fileName,
            auth.userId,
        )
    }

    @PostMapping("/webhook")
    fun webhook(
        @RequestBody body: FileWebhookDto,
    ): Boolean {
        return fileUploadSuccessUseCase.fileUploadSuccess(body.Key)
    }
}
