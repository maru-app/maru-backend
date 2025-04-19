package me.daegyeo.maru.diary.application.port.out.dto

data class UpdateDiaryDto(
    val title: String,
    val content: String,
    val emoji: String,
)
