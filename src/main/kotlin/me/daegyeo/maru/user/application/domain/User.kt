package me.daegyeo.maru.user.application.domain

import me.daegyeo.maru.shared.constant.Vendor
import java.time.ZonedDateTime
import java.util.UUID

data class User(
    val userId: UUID,
    val email: String,
    val vendor: Vendor,
    var nickname: String,
    val createdAt: ZonedDateTime,
    val updatedAt: ZonedDateTime,
    var deletedAt: ZonedDateTime?,
)
