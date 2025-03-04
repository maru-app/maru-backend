package me.daegyeo.maru.auth.application.port.`in`

fun interface IsExistsTokenInBlacklistUseCase {
    fun isExistsTokenInBlacklist(token: String): Boolean
}
