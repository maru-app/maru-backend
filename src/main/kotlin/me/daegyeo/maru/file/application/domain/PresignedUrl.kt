package me.daegyeo.maru.file.application.domain

data class PresignedUrl(
    val url: String,
    val fileName: String,
)
