package me.daegyeo.maru.user.application.service

import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.UpdateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.command.UpdateUserUseCaseCommand
import me.daegyeo.maru.user.application.port.out.UpdateUserPort
import me.daegyeo.maru.user.application.port.out.dto.UpdateUserDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UpdateUserService(private val updateUserPort: UpdateUserPort) : UpdateUserUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun updateUser(
        userId: UUID,
        input: UpdateUserUseCaseCommand,
    ): User {
        logger.info("User 데이터를 변경했습니다. $userId")
        return updateUserPort.updateUser(userId, UpdateUserDto(nickname = input.nickname, deletedAt = input.deletedAt))
            ?: throw ServiceException(UserError.USER_NOT_FOUND)
    }
}
