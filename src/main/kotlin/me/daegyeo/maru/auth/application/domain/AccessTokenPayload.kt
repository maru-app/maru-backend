package me.daegyeo.maru.auth.application.domain

import me.daegyeo.maru.shared.constant.Vendor

data class AccessTokenPayload(
    val email: String,
    val vendor: Vendor,
)
