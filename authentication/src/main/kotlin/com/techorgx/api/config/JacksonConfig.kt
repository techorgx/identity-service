package com.techorgx.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class JacksonConfig {
    @Bean
    open fun getObjectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}
