package me.daegyeo.maru.auth.application.util

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key

object KeyUtil {
    fun convertStringToKey(secret: String): Key {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
