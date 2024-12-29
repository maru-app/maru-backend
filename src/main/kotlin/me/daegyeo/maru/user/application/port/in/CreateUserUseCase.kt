package me.daegyeo.maru.user.application.port.`in`

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.`in`.command.CreateUserUseCaseCommand

fun interface CreateUserUseCase {
    fun createUser(input: CreateUserUseCaseCommand): User
}
