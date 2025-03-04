package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.port.`in`.IsExistsTokenInBlacklistUseCase
import me.daegyeo.maru.auth.application.port.out.ReadTokenBlacklistPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class IsExistsTokenInBlacklistService(
    private val readTokenBlacklistPort: ReadTokenBlacklistPort,
) : IsExistsTokenInBlacklistUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun isExistsTokenInBlacklist(token: String): Boolean {
        val result = readTokenBlacklistPort.findByToken(token)
        logger.info("블랙리스트에 토큰이 존재하는지 확인했습니다.")
        return result != null
    }
}
