package me.daegyeo.maru.diary.application.error

import me.daegyeo.maru.shared.error.BaseError

enum class DiaryError(
    override val code: String,
    override val httpCode: Int,
) : BaseError {
    DIARY_NOT_FOUND("DIARY_NOT_FOUND", 404),
    DIARY_IS_NOT_OWNED("DIARY_IS_NOT_OWNED", 403),
    DIARY_LENGTH_EXCEEDED("DIARY_LENGTH_EXCEEDED", 400),
}
