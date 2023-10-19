package com.techorgx.api.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.techorgx.api.authentication.TokenService
import com.techorgx.api.model.Customer
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Service
class CustomerService(
    @Value("\${rest.customerService.baseUrl}")
    private val baseUrl: String,
    @Value("\${rest.customerService.contentType}")
    private val contentType: String,
    @Value("\${rest.customerService.uri.addCustomer}")
    private val addCustomer: String,
    private val webClientBuilder: WebClient.Builder,
    private val tokenService: TokenService,
    private val objectMapper: ObjectMapper,
) {
    fun createCustomer(
        customer: Customer,
        claims: Map<String, String> = emptyMap(),
    ): String? {
        try {
            val webClient =
                webClientBuilder
                    .baseUrl(baseUrl)
                    .defaultHeader("Authorization", "Bearer " + tokenService.generateJwtToken(claims))
                    .build()

            return webClient.post()
                .uri(addCustomer)
                .header(CONTENT_TYPE_KEY, contentType)
                .body(BodyInserters.fromValue(objectMapper.writeValueAsString(customer)),)
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        } catch (e: Exception) {
            logger.error(e)
        }
        return null
    }

    private companion object {
        const val CONTENT_TYPE_KEY = "Content-Type"
        val logger = LogManager.getLogger(CustomerService::class.java)
    }
}
