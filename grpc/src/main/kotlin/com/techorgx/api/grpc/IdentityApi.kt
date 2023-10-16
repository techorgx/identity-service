package com.techorgx.api.grpc

import com.techorgx.identity.api.v1.ExampleApiGrpcKt
import com.techorgx.identity.api.v1.PersonRequest
import com.techorgx.identity.api.v1.PersonResponse
import org.lognet.springboot.grpc.GRpcService

@GRpcService
class IdentityApi : ExampleApiGrpcKt.ExampleApiCoroutineImplBase() {
    override suspend fun getPerson(request: PersonRequest): PersonResponse {
        return super.getPerson(request)
    }
}
