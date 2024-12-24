package me.daegyeo.maru.user.application.port.`in`.dto

import me.daegyeo.maru.user.application.constant.Vendor

data class CreateUserUseCaseDto(
    val email: String,
    val vendor: Vendor,
    val nickname: String,
)
