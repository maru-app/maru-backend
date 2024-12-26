package me.daegyeo.maru.user.application.service

import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetUserService(private val readUserPort: ReadUserPort) : GetUserUseCase {
    @Transactional
    override fun getUser(userId: UUID): User {
        val user = readUserPort.readUser(userId) ?: throw ServiceException(UserError.USER_NOT_FOUND)
        return user
    }

    @Transactional
    override fun getUserByEmail(email: String): User {
        val user = readUserPort.readUserByEmail(email) ?: throw ServiceException(UserError.USER_NOT_FOUND)
        return user
    }
}
