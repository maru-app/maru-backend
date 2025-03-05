package me.daegyeo.maru.infrastructure.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class HttpLoggingFilter : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        filterChain.doFilter(request, response)

        val realIp = request.getHeader("X-Real-IP")
        val forwardedIp = request.getHeader("X-Forwarded-For")
        val ip = realIp ?: forwardedIp ?: request.remoteAddr
        log.info(
            "[${request.method}] $ip ${request.requestURI} (${request.contentType}) - ${response.status}",
        )
    }
}
