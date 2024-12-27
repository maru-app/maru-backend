package me.daegyeo.maru.auth.application.port.`in`

import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload

interface ParseJWTUseCase {
    fun parseAccessToken(accessToken: String): AccessTokenPayload

    fun parseRegisterToken(registerToken: String): RegisterTokenPayload
}
