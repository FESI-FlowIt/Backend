package com.fesi.flowit.common.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(): WebClient {
        val httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
            .responseTimeout(Duration.ofSeconds(10))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(10))
                conn.addHandlerLast(WriteTimeoutHandler(10))
            }

        return WebClient.builder()
            .codecs { configurer ->
                configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)
            }
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .build()
    }
}