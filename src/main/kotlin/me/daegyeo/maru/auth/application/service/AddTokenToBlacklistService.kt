package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.port.`in`.AddTokenToBlacklistUseCase
import me.daegyeo.maru.auth.application.port.out.CreateTokenBlacklistPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AddTokenToBlacklistService(
    private val createTokenBlacklistPort: CreateTokenBlacklistPort,
) : AddTokenToBlacklistUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun addTokenToBlacklist(token: String): Boolean {
        createTokenBlacklistPort.createTokenBlacklist(token)
        logger.info("블랙리스트에 토큰을 추가했습니다.")
        return true
    }
}
