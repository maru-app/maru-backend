package me.daegyeo.maru.infrastructure.config

import me.daegyeo.maru.auth.application.port.`in`.OAuthUserSuccessUseCase
import me.daegyeo.maru.infrastructure.filter.ExceptionHandleFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val oAuthUserSuccessUseCase: OAuthUserSuccessUseCase) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.NEVER) }
            .oauth2Login { oauth ->
                oauth.successHandler(oAuthUserSuccessUseCase)
                oauth.authorizationEndpoint { it.baseUri("/oauth/login") }
            }
            .addFilterBefore(
                ExceptionHandleFilter(),
                UsernamePasswordAuthenticationFilter::class.java,
            )

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
