package me.daegyeo.maru.infrastructure.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig {
    @Value("\${cors.origins}")
    private lateinit var origins: String

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val parseOrigin = origins.split(",").map { it.trim() }
        val config =
            CorsConfiguration().apply {
                allowCredentials = true
                allowedOrigins = parseOrigin
                allowedMethods =
                    listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
                allowedHeaders = listOf("*")
                exposedHeaders = listOf("*")
            }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}
