package me.daegyeo.maru.auth.application.port.`in`

interface ValidateJWTUseCase {
    fun validateAccessToken(accessToken: String): Boolean

    fun validateRegisterToken(registerToken: String): Boolean
}
