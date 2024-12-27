package me.daegyeo.maru.auth.application.service

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import me.daegyeo.maru.auth.application.domain.RegisterTokenPayload
import me.daegyeo.maru.auth.application.port.`in`.GenerateJWTUseCase
import me.daegyeo.maru.auth.application.port.`in`.OAuthUserSuccessUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class OAuthUserSuccessHandler(
    private val generateJWTUseCase: GenerateJWTUseCase,
) : OAuthUserSuccessUseCase {
    @Value("\${oauth.redirect-url}")
    private lateinit var redirectUrl: String

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
        if (authentication is OAuth2AuthenticationToken) {
            val vendor = authentication.authorizedClientRegistrationId

            val oAuth2User = authentication.principal as OAuth2User
            val userAttributes = oAuth2User.attributes
            val email = userAttributes["email"] as String

            val registerToken =
                generateJWTUseCase.generateRegisterToken(
                    RegisterTokenPayload(
                        email = email,
                        vendor = vendor,
                    ),
                )

            response?.sendRedirect("$redirectUrl?token=$registerToken")
        }
    }
}
