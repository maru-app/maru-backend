package me.daegyeo.maru.auth.application.service

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import me.daegyeo.maru.auth.application.error.AuthError
import me.daegyeo.maru.auth.application.port.`in`.ValidateJWTUseCase
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ValidateJWTService : ValidateJWTUseCase {
    @Value("\${jwt.access-token.secret}")
    private lateinit var accessTokenSecret: String

    @Value("\${jwt.register-token.secret}")
    private lateinit var registerTokenSecret: String

    override fun validateAccessToken(accessToken: String): Boolean {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(accessTokenSecret.toByteArray())).build()
                .parseSignedClaims(accessToken)
            return true
        } catch (e: JwtException) {
            throw ServiceException(AuthError.PERMISSION_DENIED)
        }
    }

    override fun validateRegisterToken(registerToken: String): Boolean {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(registerTokenSecret.toByteArray())).build()
                .parseSignedClaims(registerToken)
            return true
        } catch (e: JwtException) {
            throw ServiceException(AuthError.PERMISSION_DENIED)
        }
    }
}
