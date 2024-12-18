package me.daegyeo.maru.user.application.port.out

import me.daegyeo.maru.user.application.domain.User
import java.util.UUID

interface ReadUserPort {
    fun readUser(userId: UUID): User?

    fun readUserByEmail(email: String): User?
}
