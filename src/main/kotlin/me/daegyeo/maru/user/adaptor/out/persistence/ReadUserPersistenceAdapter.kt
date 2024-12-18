package me.daegyeo.maru.user.adaptor.out.persistence

import me.daegyeo.maru.user.adaptor.out.mapper.UserMapper
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ReadUserPersistenceAdapter(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : ReadUserPort {
    override fun readUser(userId: UUID): User? {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        return userMapper.toDomain(user)
    }

    override fun readUserByEmail(email: String): User? {
        val user = userRepository.findByEmail(email).orElseThrow { RuntimeException("User not found") }
        return userMapper.toDomain(user)
    }
}
