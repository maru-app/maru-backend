package me.daegyeo.maru.user.adaptor.out.persistence

import me.daegyeo.maru.user.adaptor.out.mapper.UserMapper
import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.port.out.UpdateUserPort
import me.daegyeo.maru.user.application.port.out.dto.UpdateUserDto
import org.springframework.stereotype.Component
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

@Component
class UpdateUserPersistence(private val userRepository: UserRepository, private val userMapper: UserMapper) :
    UpdateUserPort {
    override fun updateUser(
        userId: UUID,
        inputUser: UpdateUserDto,
    ): User? {
        val user = userRepository.findById(userId).getOrNull()
        user?.let {
            if (inputUser.nickname != null) it.nickname = inputUser.nickname
            if (inputUser.deletedAt != null) it.deletedAt = inputUser.deletedAt
            return userMapper.toDomain(userRepository.save(it))
        }
        return null
    }
}
