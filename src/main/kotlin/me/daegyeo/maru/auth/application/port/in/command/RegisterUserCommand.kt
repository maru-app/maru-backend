package me.daegyeo.maru.auth.application.port.`in`.command

data class RegisterUserCommand(
    val nickname: String,
    val registerToken: String,
)
