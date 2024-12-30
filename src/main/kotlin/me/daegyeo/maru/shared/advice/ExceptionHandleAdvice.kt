package me.daegyeo.maru.shared.advice

import me.daegyeo.maru.shared.error.CommonError
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class ExceptionHandleAdvice {
    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException): ResponseEntity<*> {
        return ResponseEntity(e.error.code, HttpStatus.valueOf(e.error.httpCode))
    }

    @ExceptionHandler(MissingRequestCookieException::class)
    fun handleMissingRequestCookieException(ex: MissingRequestCookieException?): ResponseEntity<*> {
        return ResponseEntity(CommonError.COOKIE_NOT_FOUND.code, HttpStatus.valueOf(404))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException?): ResponseEntity<*> {
        return ResponseEntity(CommonError.INVALID_REQUEST.code, HttpStatus.valueOf(400))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<*> {
        ex.printStackTrace()
        return ResponseEntity(CommonError.INTERNAL_SERVER_ERROR.code, HttpStatus.valueOf(500))
    }
}
