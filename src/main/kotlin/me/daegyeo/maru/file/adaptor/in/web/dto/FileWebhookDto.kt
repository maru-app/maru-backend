package me.daegyeo.maru.file.adaptor.`in`.web.dto

data class FileWebhookDto(
    val EventName: String,
    val Key: String,
    val Records: Array<*>,
)
