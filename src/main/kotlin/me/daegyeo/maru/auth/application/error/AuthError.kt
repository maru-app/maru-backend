package me.daegyeo.maru.auth.application.error

import me.daegyeo.maru.shared.error.BaseError

enum class AuthError(
    override val code: String,
    override val httpCode: Int,
) : BaseError {
    PERMISSION_DENIED("PERMISSION_DENIED", 403),
}
