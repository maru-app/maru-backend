package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.port.`in`.ParseJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.RegisterUserUseCase
import me.daegyeo.maru.auth.application.port.`in`.command.RegisterUserCommand
import me.daegyeo.maru.auth.application.port.`in`.result.RegisterUserResult
import me.daegyeo.maru.user.application.port.`in`.CreateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.command.CreateUserUseCaseCommand
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterUserService(
    private val parseJWTUseCase: ParseJWTUseCase,
    private val createUserUseCase: CreateUserUseCase,
) : RegisterUserUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun registerUser(input: RegisterUserCommand): RegisterUserResult {
        val payload = parseJWTUseCase.parseRegisterToken(input.registerToken)

        createUserUseCase.createUser(
            CreateUserUseCaseCommand(
                email = payload.email,
                nickname = input.nickname,
                vendor = payload.vendor,
            ),
        )

        logger.info("회원가입을 했습니다. ${payload.email}")

        return RegisterUserResult(
            email = payload.email,
            vendor = payload.vendor,
        )
    }
}
