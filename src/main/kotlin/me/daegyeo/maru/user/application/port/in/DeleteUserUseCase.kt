package me.daegyeo.maru.user.application.port.`in`

import java.util.UUID

fun interface DeleteUserUseCase {
    fun deleteUser(userId: UUID)
}
