package me.daegyeo.maru.auth.adapter.out.persistence

import me.daegyeo.maru.auth.adapter.out.mapper.TokenBlacklistMapper
import me.daegyeo.maru.auth.application.domain.TokenBlacklist
import me.daegyeo.maru.auth.application.port.out.ReadTokenBlacklistPort
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class ReadTokenBlacklistPersistenceAdapter(
    private val tokenBlacklistRepository: TokenBlacklistRepository,
    private val tokenBlacklistMapper: TokenBlacklistMapper,
) : ReadTokenBlacklistPort {
    override fun findByToken(token: String): TokenBlacklist? {
        val result = tokenBlacklistRepository.findByToken(token).getOrNull()
        return result?.let {
            tokenBlacklistMapper.toDomain(it)
        }
    }
}
