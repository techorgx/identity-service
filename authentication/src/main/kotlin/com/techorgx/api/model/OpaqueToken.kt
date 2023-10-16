package com.techorgx.api.model

import java.time.ZonedDateTime

data class OpaqueToken(
    val tokenId: String = "",
    val userId: String = "",
    val expirationTimestamp: () -> ZonedDateTime = {
        ZonedDateTime.now()
    },
)
