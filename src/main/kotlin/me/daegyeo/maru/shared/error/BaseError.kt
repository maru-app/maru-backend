package me.daegyeo.maru.shared.error

interface BaseError {
    val code: String
    val httpCode: Int
}
