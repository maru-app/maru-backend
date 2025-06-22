package me.daegyeo.maru.user.application.service

import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.CreateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.command.CreateUserUseCaseCommand
import me.daegyeo.maru.user.application.port.out.CreateUserPort
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import me.daegyeo.maru.user.application.port.out.command.CreateUserPortCommand
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateUserService(private val createUserPort: CreateUserPort, private val readUserPort: ReadUserPort) :
    CreateUserUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun createUser(input: CreateUserUseCaseCommand): User {
        readUserPort.readUserByEmail(input.email).let {
            if (it != null) throw ServiceException(UserError.USER_ALREADY_EXISTS)
        }

        val createUserDto =
            CreateUserPortCommand(
                email = input.email,
                vendor = input.vendor,
                nickname = input.nickname,
            )

        val result = createUserPort.createUser(createUserDto)

        logger.info("User 데이터를 생성했습니다. ${result.userId}")

        return result
    }
}
