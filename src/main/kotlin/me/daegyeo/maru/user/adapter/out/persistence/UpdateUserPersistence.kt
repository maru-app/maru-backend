package me.daegyeo.maru.user.adapter.out.persistence

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.out.UpdateUserPort
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class UpdateUserPersistence(private val userRepository: UserRepository) :
    UpdateUserPort {
    override fun updateUser(
        userId: UUID,
        user: User,
    ): User? {
        val currentUser = userRepository.findById(userId).getOrNull()
        currentUser?.let {
            it.nickname = user.nickname
            it.isPublicRanking = user.isPublicRanking
            return User.fromEntity(userRepository.save(it))
        }
        return null
    }
}
