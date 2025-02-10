package me.daegyeo.maru.file.application.port.out.dto

import java.util.UUID

data class CreateFileDto(
    val userId: UUID,
    val path: String,
    val originalPath: String,
)
