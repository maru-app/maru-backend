package me.daegyeo.maru.infrastructure.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.daegyeo.maru.auth.application.error.AuthError
import me.daegyeo.maru.auth.application.port.`in`.CustomUserDetailsUseCase
import me.daegyeo.maru.auth.application.port.`in`.IsExistsTokenInBlacklistUseCase
import me.daegyeo.maru.auth.application.port.`in`.ParseJWTUseCase
import me.daegyeo.maru.auth.constant.Auth
import me.daegyeo.maru.shared.exception.ServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val parseJWTUseCase: ParseJWTUseCase,
    private val customUserDetailsUseCase: CustomUserDetailsUseCase,
    private val isExistsTokenInBlacklistUseCase: IsExistsTokenInBlacklistUseCase,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.cookies.isNullOrEmpty()) {
            filterChain.doFilter(request, response)
            return
        }

        val tokenCookie = request.cookies.find { it.name == Auth.ACCESS_TOKEN_COOKIE }
        if (tokenCookie != null) {
            val existsTokenInBlacklist = isExistsTokenInBlacklistUseCase.isExistsTokenInBlacklist(tokenCookie.value)
            if (existsTokenInBlacklist) {
                filterChain.doFilter(request, response)
                throw ServiceException(AuthError.PERMISSION_DENIED)
            }

            val payload = parseJWTUseCase.parseAccessToken(tokenCookie.value)
            val userDetails = customUserDetailsUseCase.loadUserByUsername(payload.email)
            val authentication =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}
