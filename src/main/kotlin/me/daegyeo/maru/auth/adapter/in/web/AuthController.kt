package me.daegyeo.maru.auth.adapter.`in`.web

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.daegyeo.maru.auth.adapter.`in`.web.dto.RegisterUserDto
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.CustomUserDetails
import me.daegyeo.maru.auth.application.port.`in`.AddTokenToBlacklistUseCase
import me.daegyeo.maru.auth.application.port.`in`.GenerateJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.GetAuthInfoUseCase
import me.daegyeo.maru.auth.application.port.`in`.RegisterUserUseCase
import me.daegyeo.maru.auth.application.port.`in`.command.RegisterUserCommand
import me.daegyeo.maru.auth.application.port.`in`.result.AuthInfoResult
import me.daegyeo.maru.auth.constant.Auth
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getAuthInfoUseCase: GetAuthInfoUseCase,
    private val generateJWTUseCase: GenerateJWTUseCase,
    private val addTokenToBlacklistUseCase: AddTokenToBlacklistUseCase,
) {
    @Value("\${domain}")
    private lateinit var domainEnv: String

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    fun getMyInfo(
        @AuthenticationPrincipal auth: CustomUserDetails,
    ): AuthInfoResult {
        return getAuthInfoUseCase.getAuthInfo(auth)
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    fun registerUser(
        @RequestBody body: RegisterUserDto,
        @RequestHeader(Auth.REGISTER_TOKEN_HEADER) registerToken: String,
        response: HttpServletResponse,
    ): Boolean {
        val result =
            registerUserUseCase.registerUser(
                RegisterUserCommand(
                    nickname = body.nickname,
                    registerToken = registerToken,
                ),
            )

        val token =
            generateJWTUseCase.generateAccessToken(AccessTokenPayload(email = result.email, vendor = result.vendor))

        response.addCookie(
            Cookie(Auth.ACCESS_TOKEN_COOKIE, token).apply {
                path = "/"
                isHttpOnly = true
                secure = true
                maxAge = (60 * 60 * 24 * 7)
                domain = domainEnv
                setAttribute("SameSite", "Lax")
            },
        )

        return true
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Boolean {
        val accessToken = request.cookies?.find { it.name == Auth.ACCESS_TOKEN_COOKIE }?.value
        if (accessToken != null) {
            addTokenToBlacklistUseCase.addTokenToBlacklist(accessToken)
        }

        response.addCookie(
            Cookie(Auth.ACCESS_TOKEN_COOKIE, null).apply {
                maxAge = 0
                isHttpOnly = true
            },
        )

        return true
    }
}
