package me.daegyeo.maru.user.adapter.out.persistence

import me.daegyeo.maru.user.application.port.out.DeleteUserPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class DeleteUserPersistenceAdapter(private val userRepository: UserRepository) : DeleteUserPort {
    override fun deleteUser(userId: UUID): Boolean {
        userRepository.deleteById(userId)
        return true
    }
}
