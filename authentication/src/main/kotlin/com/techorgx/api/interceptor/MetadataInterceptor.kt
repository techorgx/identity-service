package com.techorgx.api.interceptor

import io.grpc.Context
import io.grpc.Contexts
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import org.lognet.springboot.grpc.GRpcGlobalInterceptor

@GRpcGlobalInterceptor
class MetadataInterceptor : ServerInterceptor {
    companion object {
        val DEVICE_ID: Context.Key<String> = Context.key("device-id")
        val LOCALE: Context.Key<String> = Context.key("locale")
    }
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val deviceId = headers.get(Metadata.Key.of("device-id", Metadata.ASCII_STRING_MARSHALLER))
        val locale = headers.get(Metadata.Key.of("locale", Metadata.ASCII_STRING_MARSHALLER))
        val context: Context = Context.current()
            .withValue(DEVICE_ID, deviceId)
            .withValue(LOCALE, locale)
        return Contexts.interceptCall(context, call, headers, next)
    }
}
