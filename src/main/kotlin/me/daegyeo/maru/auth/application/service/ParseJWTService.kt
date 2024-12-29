package me.daegyeo.maru.auth.application.service

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload
import me.daegyeo.maru.auth.application.error.AuthError
import me.daegyeo.maru.auth.application.port.`in`.ParseJWTUseCase
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ParseJWTService : ParseJWTUseCase {
    @Value("\${jwt.access-token.secret}")
    private lateinit var accessTokenSecret: String

    @Value("\${jwt.register-token.secret}")
    private lateinit var registerTokenSecret: String

    override fun parseAccessToken(accessToken: String): AccessTokenPayload {
        try {
            val payload =
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(accessTokenSecret.toByteArray())).build()
                    .parseSignedClaims(accessToken).payload as Map<*, *>
            return AccessTokenPayload(
                email = payload["sub"] as String,
                vendor = Vendor.valueOf((payload["vendor"] as String).uppercase()),
            )
        } catch (e: JwtException) {
            throw ServiceException(AuthError.PERMISSION_DENIED)
        }
    }

    override fun parseRegisterToken(registerToken: String): RegisterTokenPayload {
        try {
            val payload =
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(registerTokenSecret.toByteArray())).build()
                    .parseSignedClaims(registerToken).payload as Map<*, *>
            return RegisterTokenPayload(
                email = payload["sub"] as String,
                vendor = Vendor.valueOf((payload["vendor"] as String).uppercase()),
            )
        } catch (e: JwtException) {
            throw ServiceException(AuthError.PERMISSION_DENIED)
        }
    }
}
