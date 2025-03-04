package me.daegyeo.maru.auth.application.port.`in`

fun interface AddTokenToBlacklistUseCase {
    fun addTokenToBlacklist(token: String): Boolean
}
