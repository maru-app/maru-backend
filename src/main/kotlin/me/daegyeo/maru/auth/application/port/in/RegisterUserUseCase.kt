package me.daegyeo.maru.auth.application.port.`in`

import me.daegyeo.maru.auth.application.port.`in`.command.RegisterUserCommand

fun interface RegisterUserUseCase {
    fun registerUser(input: RegisterUserCommand): Boolean
}
