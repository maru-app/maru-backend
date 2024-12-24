package me.daegyeo.maru.user.application.service

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.`in`.CreateUserUseCase
import me.daegyeo.maru.user.application.port.`in`.dto.CreateUserUseCaseDto
import me.daegyeo.maru.user.application.port.out.CreateUserPort
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import me.daegyeo.maru.user.application.port.out.dto.CreateUserDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateUserService(private val createUserPort: CreateUserPort, private val readUserPort: ReadUserPort) : CreateUserUseCase {
    @Transactional
    override fun createUser(input: CreateUserUseCaseDto): User {
        val existsUser = readUserPort.readUserByEmail(input.email)

        if (existsUser != null) {
            throw RuntimeException("User already exists")
        }

        val createUserDto =
            CreateUserDto(
                email = input.email,
                vendor = input.vendor,
                nickname = input.nickname,
            )

        return createUserPort.createUser(createUserDto)
    }
}
