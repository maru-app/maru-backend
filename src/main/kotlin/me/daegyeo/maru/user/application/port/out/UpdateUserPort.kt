package me.daegyeo.maru.user.application.port.out

import me.daegyeo.maru.user.application.domain.User
import java.util.UUID

fun interface UpdateUserPort {
    fun updateUser(
        userId: UUID,
        user: User,
    ): User?
}
