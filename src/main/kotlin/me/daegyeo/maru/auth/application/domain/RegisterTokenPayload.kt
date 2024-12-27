package me.daegyeo.maru.auth.application.domain

data class RegisterTokenPayload(
    val email: String,
    val vendor: String,
)
