package me.daegyeo.maru.user.application.service

import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import me.daegyeo.maru.user.application.port.`in`.UpdateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.command.UpdateUserUseCaseCommand
import me.daegyeo.maru.user.application.port.out.UpdateUserPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateUserService(
    private val updateUserPort: UpdateUserPort,
    private val getUserUseCase: GetUserUseCase,
) : UpdateUserUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun updateUser(
        userId: UUID,
        input: UpdateUserUseCaseCommand,
    ): User {
        val user = getUserUseCase.getUser(userId)

        if (input.nickname != user.nickname) {
            user.updateNickname(input.nickname)
        }

        if (input.isPublicRanking != user.isPublicRanking) {
            user.togglePublicRanking()
        }

        val updatedUser = updateUserPort.updateUser(userId, user) ?: throw ServiceException(UserError.USER_NOT_FOUND)

        logger.info("User 데이터를 변경했습니다. $userId")
        return updatedUser
    }
}
