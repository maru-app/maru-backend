package me.daegyeo.maru.auth.application.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload
import me.daegyeo.maru.auth.application.port.`in`.GenerateJWTUseCase
import me.daegyeo.maru.shared.util.DateFormat
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Instant
import java.util.Date

@Component
class GenerateJWTProvider : GenerateJWTUseCase {
    @Value("\${jwt.access-token.secret}")
    private lateinit var accessTokenSecret: String

    @Value("\${jwt.register-token.secret}")
    private lateinit var registerTokenSecret: String

    @Value("\${jwt.access-token.expiration}")
    private lateinit var accessTokenExpiration: String

    @Value("\${jwt.register-token.expiration}")
    private lateinit var registerTokenExpiration: String

    override fun generateAccessToken(payload: AccessTokenPayload): String {
        val token =
            Jwts.builder()
                .subject(payload.email)
                .claim("vendor", payload.vendor)
                .issuedAt(Date.from(Instant.now()))
                .expiration(DateFormat.parseDurationToDate(accessTokenExpiration))
                .signWith(convertStringToKey(accessTokenSecret))
                .compact()
        return token
    }

    override fun generateRegisterToken(payload: RegisterTokenPayload): String {
        val token =
            Jwts.builder()
                .subject(payload.email)
                .claim("vendor", payload.vendor)
                .issuedAt(Date.from(Instant.now()))
                .expiration(DateFormat.parseDurationToDate(registerTokenExpiration))
                .signWith(convertStringToKey(registerTokenSecret))
                .compact()
        return token
    }

    private fun convertStringToKey(secret: String): Key {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
