package me.daegyeo.maru.auth.application.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.daegyeo.maru.auth.application.domain.AccessTokenPayload
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload
import me.daegyeo.maru.auth.application.error.AuthError
import me.daegyeo.maru.auth.application.port.`in`.GenerateJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.OAuthUserSuccessUseCase
import me.daegyeo.maru.auth.constant.Auth
import me.daegyeo.maru.shared.constant.Vendor
import me.daegyeo.maru.shared.exception.ServiceException
import me.daegyeo.maru.user.application.error.UserError
import me.daegyeo.maru.user.application.port.`in`.GetUserUseCase
import org.slf4j.LoggerFactory
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

    @Value("\${oauth.success-url}")
    private lateinit var successUrl: String

    @Value("\${domain}")
    private lateinit var domain: String

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        if (authentication !is OAuth2AuthenticationToken) return

        val oAuth2User = authentication.principal as OAuth2User
        val vendor = authentication.authorizedClientRegistrationId
        val userAttributes = oAuth2User.attributes
        val email =
            when (vendor) {
                Vendor.GOOGLE.name.lowercase() -> userAttributes["email"] as String
                Vendor.NAVER.name.lowercase() -> (userAttributes["response"] as Map<String, String>)["email"] as String
                else -> throw ServiceException(AuthError.PERMISSION_DENIED)
            }

        try {
            val existsUser = getUserUseCase.getUserByEmail(email)
            val token =
                generateJWTUseCase.generateAccessToken(
                    AccessTokenPayload(
                        email = existsUser.email,
                        vendor = existsUser.vendor,
                    ),
                )

            val tokenCookie =
                Cookie(Auth.ACCESS_TOKEN_COOKIE, token).apply {
                    path = "/"
                    isHttpOnly = true
                    secure = true
                    maxAge = (60 * 60 * 24 * 7)
                    this.domain = domain
                }
            response?.addCookie(tokenCookie)
            response?.sendRedirect(successUrl)

            logger.info("소셜 로그인에 성공했습니다. $email")
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

                logger.info("소셜 로그인으로 신규 가입을 진행했습니다. $email")
            } else {
                throw e
            }
        }
    }
}
