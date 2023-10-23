package com.techorgx.api.utility

import com.techorgx.api.interceptor.MetadataInterceptor
import io.grpc.Context
import org.springframework.stereotype.Component

@Component
class MetadataUtils {
    fun getDeviceId(): String {
        return MetadataInterceptor.DEVICE_ID.get(Context.current())
    }

    fun getLocale(): String {
        return MetadataInterceptor.LOCALE.get(Context.current())
    }
}
