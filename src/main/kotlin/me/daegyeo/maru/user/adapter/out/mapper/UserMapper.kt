package me.daegyeo.maru.user.adapter.out.mapper

import me.daegyeo.maru.user.application.domain.User
import me.daegyeo.maru.user.application.persistence.UserEntity
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toDomain(userEntity: UserEntity): User {
        return User(
            userId = userEntity.userId!!,
            email = userEntity.email,
            vendor = userEntity.vendor,
            nickname = userEntity.nickname,
            createdAt = userEntity.createdAt!!,
            updatedAt = userEntity.updatedAt!!,
            deletedAt = userEntity.deletedAt,
        )
    }

    fun toEntity(user: User): UserEntity {
        return UserEntity(
            userId = user.userId,
            email = user.email,
            vendor = user.vendor,
            nickname = user.nickname,
            deletedAt = user.deletedAt,
        )
    }
}
