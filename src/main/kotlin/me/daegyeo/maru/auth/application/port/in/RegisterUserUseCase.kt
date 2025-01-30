package me.daegyeo.maru.auth.application.port.`in`

import me.daegyeo.maru.auth.application.port.`in`.command.RegisterUserCommand
import me.daegyeo.maru.auth.application.port.`in`.result.RegisterUserResult

fun interface RegisterUserUseCase {
    fun registerUser(input: RegisterUserCommand): RegisterUserResult
}
