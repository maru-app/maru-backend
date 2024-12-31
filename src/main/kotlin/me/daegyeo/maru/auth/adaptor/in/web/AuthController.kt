package me.daegyeo.maru.auth.adaptor.`in`.web

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import me.daegyeo.maru.auth.adaptor.`in`.web.dto.RegisterUserDto
import me.daegyeo.maru.auth.application.port.`in`.GetAuthInfoUseCase
import me.daegyeo.maru.auth.application.port.`in`.RegisterUserUseCase
import me.daegyeo.maru.auth.application.port.`in`.command.RegisterUserCommand
import me.daegyeo.maru.auth.application.port.`in`.result.AuthInfoResult
import me.daegyeo.maru.auth.constant.Auth
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getAuthInfoUseCase: GetAuthInfoUseCase,
) {
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    fun getMyInfo(
        @CookieValue(Auth.ACCESS_TOKEN_COOKIE) accessToken: String,
    ): AuthInfoResult {
        return getAuthInfoUseCase.getAuthInfo(accessToken)
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    fun registerUser(
        @RequestBody body: RegisterUserDto,
        @RequestHeader(Auth.REGISTER_TOKEN_HEADER) registerToken: String,
    ): Boolean {
        return registerUserUseCase.registerUser(
            RegisterUserCommand(
                nickname = body.nickname,
                registerToken = registerToken,
            ),
        )
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/logout")
    fun logout(response: HttpServletResponse): Boolean {
        response.addCookie(
            Cookie(Auth.ACCESS_TOKEN_COOKIE, null).apply {
                maxAge = 0
                isHttpOnly = true
            },
        )
        // TODO: Add the deleted access token in blacklist database.
        return true
    }
}
