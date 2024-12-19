package me.daegyeo.maru.user.application.port.out

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.out.dto.CreateUserDto

fun interface CreateUserPort {
    fun createUser(inputUser: CreateUserDto): User
}
