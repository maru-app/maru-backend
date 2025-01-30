package me.daegyeo.maru.auth.application.port.`in`.result

import me.daegyeo.maru.shared.constant.Vendor

data class RegisterUserResult(
    val email: String,
    val vendor: Vendor,
)
