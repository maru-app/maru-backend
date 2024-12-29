package me.daegyeo.maru.user.application.port.`in`.command

import java.time.ZonedDateTime

data class UpdateUserUseCaseCommand(
    val nickname: String?,
    val deletedAt: ZonedDateTime?,
)
