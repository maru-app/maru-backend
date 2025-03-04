package me.daegyeo.maru.auth.application.port.out

import me.daegyeo.maru.auth.application.domain.TokenBlacklist

fun interface ReadTokenBlacklistPort {
    fun findByToken(token: String): TokenBlacklist?
}
