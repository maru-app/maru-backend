package me.daegyeo.maru.file.adapter.`in`.web.dto

data class FileWebhookDto(
    val EventName: String,
    val Key: String,
    val Records: Array<*>,
)
