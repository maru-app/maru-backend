package me.daegyeo.maru.user.application.port.`in`

import me.daegyeo.maru.user.application.domain.User
import java.util.UUID

interface GetUserUseCase {
    fun getUser(userId: UUID): User

    fun getUserByEmail(email: String): User
}
