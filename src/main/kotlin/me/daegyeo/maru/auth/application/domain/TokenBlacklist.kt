package me.daegyeo.maru.auth.application.domain

import java.time.ZonedDateTime

data class TokenBlacklist(
    val tokenBlacklistId: Long,
    val token: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
)
