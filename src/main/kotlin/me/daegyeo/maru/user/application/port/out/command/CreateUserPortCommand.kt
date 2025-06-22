package me.daegyeo.maru.user.application.port.out.command

import me.daegyeo.maru.shared.constant.Vendor

data class CreateUserPortCommand(
    val email: String,
    val vendor: Vendor,
    val nickname: String,
)
