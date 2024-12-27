package me.daegyeo.maru.auth.application.domain

data class AccessTokenPayload(
    val email: String,
    val vendor: String,
)
