package me.daegyeo.maru.auth.adapter.out.persistence

import me.daegyeo.maru.auth.application.persistence.TokenBlacklistEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface TokenBlacklistRepository : JpaRepository<TokenBlacklistEntity, Long> {
    fun findByToken(token: String): Optional<TokenBlacklistEntity>
}
