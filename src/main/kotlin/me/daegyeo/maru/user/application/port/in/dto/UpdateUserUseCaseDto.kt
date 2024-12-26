package me.daegyeo.maru.user.application.port.`in`.dto

import java.time.ZonedDateTime

data class UpdateUserUseCaseDto(
    val nickname: String?,
    val deletedAt: ZonedDateTime?,
)
