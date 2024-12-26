package me.daegyeo.maru.user.application.port.out.dto

import java.time.ZonedDateTime

data class UpdateUserDto(
    val nickname: String?,
    val deletedAt: ZonedDateTime?,
)
