package com.fesi.flowit.common.config

import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {

    @Bean
    fun restClient(): RestClient {
        val requestConfig: RequestConfig = RequestConfig.custom()
            .setConnectTimeout(10_000, java.util.concurrent.TimeUnit.MILLISECONDS) // 연결 타임아웃
            .setResponseTimeout(10_000, java.util.concurrent.TimeUnit.MILLISECONDS) // 읽기/쓰기 타임아웃
            .build()

        val httpClient: CloseableHttpClient = HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory(httpClient)

        return RestClient.builder()
            .requestFactory(requestFactory)
            .build()
    }
}

