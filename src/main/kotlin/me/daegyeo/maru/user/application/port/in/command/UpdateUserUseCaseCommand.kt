package me.daegyeo.maru.user.application.port.`in`.command

data class UpdateUserUseCaseCommand(
    val nickname: String,
    val isPublicRanking: Boolean,
)
