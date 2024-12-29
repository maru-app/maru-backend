package me.daegyeo.maru.auth.application.port.`in`

import me.daegyeo.maru.auth.application.port.`in`.result.AuthInfoResult

fun interface GetAuthInfoUseCase {
    fun getAuthInfo(accessToken: String): AuthInfoResult
}
