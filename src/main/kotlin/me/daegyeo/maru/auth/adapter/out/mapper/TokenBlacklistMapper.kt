package me.daegyeo.maru.auth.adapter.out.mapper

import me.daegyeo.maru.auth.application.domain.TokenBlacklist
import me.daegyeo.maru.auth.application.persistence.TokenBlacklistEntity
import org.springframework.stereotype.Component

@Component
class TokenBlacklistMapper {
    fun toDomain(tokenBlacklistEntity: TokenBlacklistEntity): TokenBlacklist {
        return TokenBlacklist(
            tokenBlacklistId = tokenBlacklistEntity.tokenBlacklistId!!,
            token = tokenBlacklistEntity.token,
            createdAt = tokenBlacklistEntity.createdAt!!,
            updatedAt = tokenBlacklistEntity.updatedAt!!,
        )
    }
}
