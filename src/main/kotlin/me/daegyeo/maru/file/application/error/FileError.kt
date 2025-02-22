package me.daegyeo.maru.file.application.error

import me.daegyeo.maru.shared.error.BaseError

enum class FileError(
    override val code: String,
    override val httpCode: Int,
) : BaseError {
    FILE_NOT_FOUND("FILE_NOT_FOUND", 404),
}
