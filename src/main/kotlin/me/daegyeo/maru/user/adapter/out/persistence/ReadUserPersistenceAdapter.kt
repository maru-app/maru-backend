package me.daegyeo.maru.user.adapter.out.persistence

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.out.ReadUserPort
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class ReadUserPersistenceAdapter(
    private val userRepository: UserRepository,
) : ReadUserPort {
    override fun readUser(userId: UUID): User? {
        val user = userRepository.findById(userId).getOrNull()
        return user?.let { User.fromEntity(it) }
    }

    override fun readUserByEmail(email: String): User? {
        val user = userRepository.findByEmail(email).getOrNull()
        return user?.let { User.fromEntity(it) }
    }
}
