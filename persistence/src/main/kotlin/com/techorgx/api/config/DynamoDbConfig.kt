package com.techorgx.api.config

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.techorgx.api.util.DynamoBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class DynamoDbConfig(
    private val dynamoBuilder: DynamoBuilder,
) {
    @Bean
    open fun getDynamoDbMapper(): DynamoDBMapper {
        return dynamoBuilder.awsDynamoDbClient
    }
}
