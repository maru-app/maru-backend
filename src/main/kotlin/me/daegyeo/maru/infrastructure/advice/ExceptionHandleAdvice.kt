package me.daegyeo.maru.infrastructure.advice

import me.daegyeo.maru.shared.error.CommonError
import me.daegyeo.maru.shared.error.ErrorResponse
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandleAdvice {
    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(e.error.code), HttpStatus.valueOf(e.error.httpCode))
    }

    @ExceptionHandler(MissingRequestCookieException::class)
    fun handleMissingRequestCookieException(): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(CommonError.COOKIE_NOT_FOUND.code),
            HttpStatus.valueOf(404),
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class, HttpRequestMethodNotSupportedException::class)
    fun handleMethodArgumentNotValidException(): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(CommonError.INVALID_REQUEST.code),
            HttpStatus.valueOf(400),
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        e.printStackTrace()
        return ResponseEntity(
            ErrorResponse(CommonError.INTERNAL_SERVER_ERROR.code),
            HttpStatus.valueOf(500),
        )
    }
}
