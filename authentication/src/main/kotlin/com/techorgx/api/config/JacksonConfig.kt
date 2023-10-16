package com.techorgx.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {
    @Bean
    fun getObjectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}
