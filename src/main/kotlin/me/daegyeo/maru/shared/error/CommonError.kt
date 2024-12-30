package me.daegyeo.maru.shared.error

enum class CommonError(
    override val code: String,
    override val httpCode: Int,
) : BaseError {
    COOKIE_NOT_FOUND("COOKIE_NOT_FOUND", 404),
    INVALID_REQUEST("INVALID_REQUEST", 400),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", 500),
}
