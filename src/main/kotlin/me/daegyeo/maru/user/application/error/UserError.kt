package me.daegyeo.maru.user.application.error

import me.daegyeo.maru.shared.error.BaseError

enum class UserError(
    override val code: String,
    override val httpCode: Int,
) : BaseError {
    USER_NOT_FOUND("USER_NOT_FOUND", 404),
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", 409),
}
