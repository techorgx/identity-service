package com.techorgx.api.cache

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.techorgx.api.model.OpaqueToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
@Deprecated("Cache will be moved to Redis")
class CacheService(
    @Value("\${security.token.opaqueTokenTtlHrs}")
    private val opaqueTokenTtlHrs: Long,
) {
    val cache: Cache<String, OpaqueToken> =
        CacheBuilder.newBuilder()
            .maximumSize(
                1000,
            ) // move this cache to Redis server on a separate pod to avoid memory issues, for now it is ok to use google cache.
            .expireAfterWrite(opaqueTokenTtlHrs, TimeUnit.HOURS)
            .build()
}
