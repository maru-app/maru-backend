package me.daegyeo.maru.auth.adapter.out.persistence

import me.daegyeo.maru.auth.adapter.out.mapper.TokenBlacklistMapper
import me.daegyeo.maru.auth.application.domain.TokenBlacklist
import me.daegyeo.maru.auth.application.persistence.TokenBlacklistEntity
import me.daegyeo.maru.auth.application.port.out.CreateTokenBlacklistPort
import org.springframework.stereotype.Component

@Component
class CreateTokenBlacklistPersistenceAdapter(
    private val tokenBlacklistRepository: TokenBlacklistRepository,
    private val tokenBlacklistMapper: TokenBlacklistMapper,
) : CreateTokenBlacklistPort {
    override fun createTokenBlacklist(token: String): TokenBlacklist {
        val entity = TokenBlacklistEntity(token = token)
        val result = tokenBlacklistRepository.save(entity)
        return tokenBlacklistMapper.toDomain(result)
    }
}
