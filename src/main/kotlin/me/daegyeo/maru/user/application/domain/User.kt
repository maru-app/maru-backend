package me.daegyeo.maru.user.application.domain

import me.daegyeo.maru.shared.constant.Vendor

data class User(
    val userId: String,
    val vendor: Vendor,
    val nickname: String,
    val deletedAt: String,
    val createdAt: String,
    val updatedAt: String,
)
