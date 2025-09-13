package com.fesi.flowit.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fesi.flowit.common.auth.CustomAccessDeniedHandler
import com.fesi.flowit.common.auth.CustomAuthenticationEntryPointHandler
import com.fesi.flowit.common.auth.JwtAuthenticationFilter
import com.fesi.flowit.common.auth.SocialAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
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
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val objectMapper: ObjectMapper,
    private val userDetailsService: UserDetailsService
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val filterChain = http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.POST, "/users").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users").permitAll()
                    .requestMatchers("/auths/**").permitAll()
                    .requestMatchers(
                        "/swagger-ui/**",  // Swagger UI 정적 리소스
                        "/v3/api-docs/**", // Swagger 문서 API
                        "/api-doc/**"
                    ).permitAll()
                    .requestMatchers("/users/me").authenticated()
                    .requestMatchers(
                        "/goals/**",
                        "/todos/**",
                        "/schedules/**",
                        "/notes/**"
                    ).authenticated()
                    .anyRequest().permitAll()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPointHandler(objectMapper))
                it.accessDeniedHandler(CustomAccessDeniedHandler(objectMapper))
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
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("*")
        configuration.allowCredentials = false

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)

        val daoAuthenticationProvider = DaoAuthenticationProvider(userDetailsService)
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        authManagerBuilder.authenticationProvider(daoAuthenticationProvider)

        authManagerBuilder.authenticationProvider(SocialAuthenticationProvider(userDetailsService))

        return authManagerBuilder.build()
    }
}
