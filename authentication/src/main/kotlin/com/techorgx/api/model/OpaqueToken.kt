package com.techorgx.api.model

import java.time.ZonedDateTime

data class OpaqueToken(
    var tokenId: String = "",
    var username: String = "",
    var expirationTimestamp: ZonedDateTime = ZonedDateTime.now(),
)
