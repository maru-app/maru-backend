package me.daegyeo.maru.user.application.port.`in`.command

import me.daegyeo.maru.shared.constant.Vendor

data class CreateUserUseCaseCommand(
    val email: String,
    val vendor: Vendor,
    val nickname: String,
)
