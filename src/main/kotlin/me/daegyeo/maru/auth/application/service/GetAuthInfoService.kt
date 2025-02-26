package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.port.`in`.GetAuthInfoUseCase
import me.daegyeo.maru.auth.application.port.`in`.ParseJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.result.AuthInfoResult
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAuthInfoService(private val getUserUseCase: GetUserUseCase, private val parseJWTUseCase: ParseJWTUseCase) :
    GetAuthInfoUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional(readOnly = true)
    override fun getAuthInfo(accessToken: String): AuthInfoResult {
        val payload = parseJWTUseCase.parseAccessToken(accessToken)
        val user = getUserUseCase.getUserByEmail(payload.email)
        logger.info("User 인증 정보를 조회했습니다. ${user.email}")
        return AuthInfoResult(
            email = user.email,
            nickname = user.nickname,
            vendor = user.vendor,
            createdAt = user.createdAt,
        )
    }
}
