package me.daegyeo.maru.auth.application.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload
import me.daegyeo.maru.auth.application.port.`in`.GenerateJWTUseCase
import me.daegyeo.maru.shared.util.DateFormat
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date

@Service
class GenerateJWTService : GenerateJWTUseCase {
    @Value("\${jwt.access-token.secret}")
    private lateinit var accessTokenSecret: String

    @Value("\${jwt.register-token.secret}")
    private lateinit var registerTokenSecret: String

    @Value("\${jwt.access-token.expiration}")
    private lateinit var accessTokenExpiration: String

    @Value("\${jwt.register-token.expiration}")
    private lateinit var registerTokenExpiration: String

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun generateAccessToken(payload: AccessTokenPayload): String {
        val token =
            Jwts.builder()
                .subject(payload.email)
                .claim("vendor", payload.vendor)
                .issuedAt(Date.from(Instant.now()))
                .expiration(DateFormat.parseDurationToDate(accessTokenExpiration))
                .signWith(Keys.hmacShaKeyFor(accessTokenSecret.toByteArray()))
                .compact()
        logger.info("Access Token을 생성했습니다. email: ${payload.email}")
        return token
    }

    override fun generateRegisterToken(payload: RegisterTokenPayload): String {
        val token =
            Jwts.builder()
                .subject(payload.email)
                .claim("vendor", payload.vendor)
                .issuedAt(Date.from(Instant.now()))
                .expiration(DateFormat.parseDurationToDate(registerTokenExpiration))
                .signWith(Keys.hmacShaKeyFor(registerTokenSecret.toByteArray()))
                .compact()
        logger.info("Register Token을 생성했습니다. email: ${payload.email}")
        return token
    }
}
