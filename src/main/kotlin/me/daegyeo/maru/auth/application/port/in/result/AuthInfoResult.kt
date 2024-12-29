package me.daegyeo.maru.auth.application.port.`in`.result

import me.daegyeo.maru.shared.constant.Vendor
import java.time.ZonedDateTime

data class AuthInfoResult(
    val email: String,
    val nickname: String,
    val vendor: Vendor,
    val createdAt: ZonedDateTime,
)
