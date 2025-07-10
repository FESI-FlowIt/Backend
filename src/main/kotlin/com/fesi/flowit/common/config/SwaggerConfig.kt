package com.fesi.flowit.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(listOf(Server().url("-")))
    }

    private fun apiInfo(): Info {
        return Info()
            .title("FlowIt")
            .description("FlowIt API Specification")
            .version("1.0.0")
    }
}