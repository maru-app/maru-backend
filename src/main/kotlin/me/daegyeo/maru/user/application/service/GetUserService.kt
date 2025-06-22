package me.daegyeo.maru.user.application.service

import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetUserService(private val readUserPort: ReadUserPort) : GetUserUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getUser(userId: UUID): User {
        logger.info("User 데이터를 조회했습니다. $userId")
        val user = readUserPort.readUser(userId) ?: throw ServiceException(UserError.USER_NOT_FOUND)
        return user
    }

    override fun getUserByEmail(email: String): User {
        logger.info("User 데이터를 조회했습니다. $email")
        val user = readUserPort.readUserByEmail(email) ?: throw ServiceException(UserError.USER_NOT_FOUND)
        return user
    }
}
