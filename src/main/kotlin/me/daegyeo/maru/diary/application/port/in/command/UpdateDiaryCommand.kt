package me.daegyeo.maru.diary.application.port.`in`.command

data class UpdateDiaryCommand(
    val title: String,
    val content: String,
)
