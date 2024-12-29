package me.daegyeo.maru.auth.application.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload
import me.daegyeo.maru.auth.application.port.`in`.GenerateJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.OAuthUserSuccessUseCase
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class OAuthUserSuccessHandler(
    private val generateJWTUseCase: GenerateJWTUseCase,
    private val getUserUseCase: GetUserUseCase,
) : OAuthUserSuccessUseCase {
    @Value("\${oauth.redirect-url}")
    private lateinit var redirectUrl: String

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        if (authentication !is OAuth2AuthenticationToken) return

        val oAuth2User = authentication.principal as OAuth2User
        val userAttributes = oAuth2User.attributes
        val email = userAttributes["email"] as String
        val vendor = authentication.authorizedClientRegistrationId

        try {
            val existsUser = getUserUseCase.getUserByEmail(email)
            val token =
                generateJWTUseCase.generateAccessToken(
                    AccessTokenPayload(
                        email = existsUser.email,
                        vendor = existsUser.vendor,
                    ),
                )

            response?.addCookie(Cookie("_maruToken", token))
            response?.sendRedirect("/")
        } catch (e: Exception) {
            if (e is ServiceException && e.error == UserError.USER_NOT_FOUND) {
                val registerToken =
                    generateJWTUseCase.generateRegisterToken(
                        RegisterTokenPayload(
                            email = email,
                            vendor = Vendor.valueOf(vendor.uppercase()),
                        ),
                    )

                response?.sendRedirect("$redirectUrl?token=$registerToken")
            } else {
                throw e
            }
        }
    }
}
