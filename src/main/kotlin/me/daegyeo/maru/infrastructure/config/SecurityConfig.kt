package me.daegyeo.maru.infrastructure.config

import me.daegyeo.maru.auth.application.error.AuthError
import me.daegyeo.maru.auth.application.port.`in`.CustomUserDetailsUseCase
import me.daegyeo.maru.auth.application.port.`in`.OAuthUserSuccessUseCase
import me.daegyeo.maru.auth.application.port.`in`.ParseJWTUseCase
import me.daegyeo.maru.infrastructure.filter.ExceptionHandleFilter
import me.daegyeo.maru.infrastructure.filter.JwtAuthenticationFilter
import me.daegyeo.maru.shared.exception.ServiceException
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
class SecurityConfig(
    private val oAuthUserSuccessUseCase: OAuthUserSuccessUseCase,
    private val parseJWTUseCase: ParseJWTUseCase,
    private val customUserDetailsUseCase: CustomUserDetailsUseCase,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .logout { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .oauth2Login { oauth ->
                oauth.successHandler(oAuthUserSuccessUseCase)
                oauth.authorizationEndpoint { it.baseUri("/oauth/login") }
            }
            .authorizeHttpRequests { authorize ->
                authorize.requestMatchers("/oauth/login").permitAll()
                authorize.requestMatchers("/login/oauth2/code/**").permitAll()
                authorize.requestMatchers("/auth/register").permitAll()
                authorize.anyRequest().authenticated()
            }
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint { _, _, _ ->
                    throw ServiceException(AuthError.PERMISSION_DENIED)
                }
                exception.accessDeniedHandler { _, _, _ ->
                    throw ServiceException(AuthError.PERMISSION_DENIED)
                }
            }
            .addFilterBefore(
                ExceptionHandleFilter(),
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .addFilterBefore(
                JwtAuthenticationFilter(parseJWTUseCase, customUserDetailsUseCase),
                UsernamePasswordAuthenticationFilter::class.java,
            )

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
