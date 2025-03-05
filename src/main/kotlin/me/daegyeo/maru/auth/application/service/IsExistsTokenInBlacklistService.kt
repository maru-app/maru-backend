package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.port.`in`.IsExistsTokenInBlacklistUseCase
import me.daegyeo.maru.auth.application.port.out.ReadTokenBlacklistPort
import org.springframework.stereotype.Service

@Service
class IsExistsTokenInBlacklistService(
    private val readTokenBlacklistPort: ReadTokenBlacklistPort,
) : IsExistsTokenInBlacklistUseCase {
    override fun isExistsTokenInBlacklist(token: String): Boolean {
        val result = readTokenBlacklistPort.findByToken(token)
        return result != null
    }
}
