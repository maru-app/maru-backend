package me.daegyeo.maru.auth.application.port.`in`

import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload

interface GenerateJWTUseCase {
    fun generateAccessToken(payload: AccessTokenPayload): String

    fun generateRegisterToken(payload: RegisterTokenPayload): String
}
