package me.daegyeo.maru.user.adapter.out.persistence

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.persistence.UserEntity
import me.daegyeo.maru.user.application.port.out.CreateUserPort
import me.daegyeo.maru.user.application.port.out.command.CreateUserPortCommand
import org.springframework.stereotype.Component

@Component
class CreateUserPersistenceAdapter(
    private val userRepository: UserRepository,
) : CreateUserPort {
    override fun createUser(inputUser: CreateUserPortCommand): User {
        val user =
            UserEntity(
                email = inputUser.email,
                vendor = inputUser.vendor,
                nickname = inputUser.nickname,
                deletedAt = null,
            )

        val createdUser = userRepository.save(user)

        return User.fromEntity(createdUser)
    }
}
