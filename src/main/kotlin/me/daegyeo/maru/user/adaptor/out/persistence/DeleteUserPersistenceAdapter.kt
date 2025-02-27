package me.daegyeo.maru.user.adaptor.out.persistence

import me.daegyeo.maru.user.application.port.out.DeleteUserPort
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class DeleteUserPersistenceAdapter(private val userRepository: UserRepository) : DeleteUserPort {
    override fun deleteUser(userId: UUID): Boolean {
        val user = userRepository.findById(userId).getOrNull()
        return user?.let {
            userRepository.delete(it)
            true
        } ?: false
    }
}
