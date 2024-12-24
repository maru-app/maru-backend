package me.daegyeo.maru.user.adaptor.out.persistence

import me.daegyeo.maru.user.adaptor.out.mapper.UserMapper
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class ReadUserPersistenceAdapter(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : ReadUserPort {
    override fun readUser(userId: UUID): User? {
        val user = userRepository.findById(userId).getOrNull()
        return user?.let { userMapper.toDomain(it) }
    }

    override fun readUserByEmail(email: String): User? {
        val user = userRepository.findByEmail(email).getOrNull()
        return user?.let { userMapper.toDomain(it) }
    }
}
