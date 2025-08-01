package com.fesi.flowit.common.config

import com.fesi.flowit.common.auth.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val filterChain = http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/users/**", "/auths/**", // 로그인/회원가입/토큰 재발급 등
                        "/swagger-ui/**",          // Swagger UI 정적 리소스
                        "/v3/api-docs/**",         // Swagger 문서 API
                        "/api-doc/**"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            .build()

        return filterChain
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://3.35.108.14", "http://localhost:3000")
        configuration.allowedMethods = listOf("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf(
            "Authorization",
            "Content-Type",
            "Cache-Control",
            "X-Requested-With",
            "Accept",
            "Origin"
        )
        configuration.exposedHeaders = listOf("Authorization", "Content-Type")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authenticationManager(authenticationContiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationContiguration.getAuthenticationManager()
    }
}
