package me.daegyeo.maru.auth.application.service

import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.auth.application.port.`in`.GetAuthInfoUseCase
import me.daegyeo.maru.auth.application.port.`in`.result.AuthInfoResult
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class GetAuthInfoService : GetAuthInfoUseCase {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun getAuthInfo(customUserDetails: CustomUserDetails): AuthInfoResult {
        logger.info("User 인증 정보를 조회했습니다. ${customUserDetails.username}")
        return AuthInfoResult(
            email = customUserDetails.username,
            nickname = customUserDetails.nickname,
            vendor = customUserDetails.vendor,
            createdAt = customUserDetails.createdAt,
        )
    }
}
