package me.daegyeo.maru.infrastructure.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.sentry.Sentry
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.daegyeo.maru.shared.error.BaseError
import me.daegyeo.maru.shared.error.CommonError
import me.daegyeo.maru.shared.error.ErrorResponse
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.shared.util.IPAddress
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter

class ExceptionHandleFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: ServiceException) {
            sendErrorResponse(response, e.error)
            log.warn(
                "[${request.method}] ${IPAddress.getClientIp(request)} ${request.requestURI} (${request.contentType}) - ${response.status}",
            )
        } catch (e: Exception) {
            e.printStackTrace()
            sendErrorResponse(response, CommonError.INTERNAL_SERVER_ERROR)
            Sentry.captureException(e)
        }
    }

    private fun sendErrorResponse(
        response: HttpServletResponse,
        errorCode: BaseError,
    ) {
        response.status = errorCode.httpCode
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json"

        val objectMapper = ObjectMapper()
        val result = mapOf("error" to ErrorResponse(errorCode.code))
        response.writer.write(objectMapper.writeValueAsString(result))
    }
}
