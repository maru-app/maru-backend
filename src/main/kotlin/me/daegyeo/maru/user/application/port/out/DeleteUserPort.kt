package me.daegyeo.maru.user.application.port.out

import java.util.UUID

fun interface DeleteUserPort {
    fun deleteUser(userId: UUID): Boolean
}
