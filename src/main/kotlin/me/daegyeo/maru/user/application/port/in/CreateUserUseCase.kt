package me.daegyeo.maru.user.application.port.`in`

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.`in`.dto.CreateUserUseCaseDto

fun interface CreateUserUseCase {
    fun createUser(input: CreateUserUseCaseDto): User
}
