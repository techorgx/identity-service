package com.techorgx.api.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun getWebClient(): WebClient.Builder {
        return WebClient.builder()
    }
}
