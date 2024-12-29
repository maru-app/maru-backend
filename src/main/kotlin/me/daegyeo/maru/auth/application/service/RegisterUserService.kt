package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.port.`in`.ParseJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.RegisterUserUseCase
import me.daegyeo.maru.auth.application.port.`in`.command.RegisterUserCommand
import me.daegyeo.maru.user.application.port.`in`.CreateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.dto.CreateUserUseCaseDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterUserService(
    private val parseJWTUseCase: ParseJWTUseCase,
    private val createUserUseCase: CreateUserUseCase,
) : RegisterUserUseCase {
    @Transactional
    override fun registerUser(input: RegisterUserCommand): Boolean {
        val payload = parseJWTUseCase.parseRegisterToken(input.registerToken)

        createUserUseCase.createUser(
            CreateUserUseCaseDto(
                email = payload.email,
                nickname = input.nickname,
                vendor = payload.vendor,
            ),
        )

        return true
    }
}
