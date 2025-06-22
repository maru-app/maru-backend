package me.daegyeo.maru.user.application.domain

import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.user.application.persistence.UserEntity
import java.time.ZonedDateTime
import java.util.UUID

class User(
    val userId: UUID,
    val email: String,
    val vendor: Vendor,
    var nickname: String,
    var isPublicRanking: Boolean,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    var deletedAt: ZonedDateTime? = null,
) {
    fun updateNickname(newNickname: String) {
        this.nickname = newNickname
    }

    fun togglePublicRanking() {
        this.isPublicRanking = !this.isPublicRanking
    }

    fun toEntity(): UserEntity {
        return UserEntity(
            userId = this.userId,
            email = this.email,
            vendor = this.vendor,
            nickname = this.nickname,
            isPublicRanking = this.isPublicRanking,
            deletedAt = this.deletedAt,
        )
    }

    companion object {
        /**
         * userId, createdAt, updatedAt 는 DBMS에 의해 자동으로 생성됩니다.
         */
        fun fromEntity(entity: UserEntity): User {
            return User(
                userId = entity.userId ?: UUID.randomUUID(),
                email = entity.email,
                vendor = entity.vendor,
                nickname = entity.nickname,
                isPublicRanking = entity.isPublicRanking,
                createdAt = entity.createdAt ?: ZonedDateTime.now(),
                updatedAt = entity.updatedAt ?: ZonedDateTime.now(),
                deletedAt = entity.deletedAt,
            )
        }
    }
}
