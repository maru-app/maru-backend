package me.daegyeo.maru.auth.application.error

import me.daegyeo.maru.shared.error.BaseError

enum class AuthError(
    val code: String,
    val httpCode: Int,
) : BaseError {
    PERMISSION_DENIED("PERMISSION_DENIED", 403),
}
