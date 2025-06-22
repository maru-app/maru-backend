package me.daegyeo.maru.user.application.port.out

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.out.command.CreateUserPortCommand

fun interface CreateUserPort {
    fun createUser(inputUser: CreateUserPortCommand): User
}
