package com.techorgx.api.grpc

import com.techorgx.api.service.IdentityService
import com.techorgx.identity.api.v1.CreateUserRequest
import com.techorgx.identity.api.v1.CreateUserResponse
import com.techorgx.identity.api.v1.IdentityApiGrpcKt
import com.techorgx.identity.api.v1.LoginUserRequest
import com.techorgx.identity.api.v1.LoginUserResponse
import com.techorgx.identity.api.v1.VerifyEmailOtpRequest
import com.techorgx.identity.api.v1.VerifyEmailOtpResponse
import com.techorgx.identity.api.v1.VerifyEmailRequest
import com.techorgx.identity.api.v1.VerifyEmailResponse
import org.lognet.springboot.grpc.GRpcService

@GRpcService
class IdentityApi(
    private val identityService: IdentityService,
) : IdentityApiGrpcKt.IdentityApiCoroutineImplBase() {
    override suspend fun createUser(request: CreateUserRequest): CreateUserResponse {
        return identityService.createUser(request)
    }

    override suspend fun loginUser(request: LoginUserRequest): LoginUserResponse {
        return identityService.loginUser(request)
    }

    override suspend fun verifyEmail(request: VerifyEmailRequest): VerifyEmailResponse {
        return super.verifyEmail(request)
    }

    override suspend fun verifyEmailOtp(request: VerifyEmailOtpRequest): VerifyEmailOtpResponse {
        return super.verifyEmailOtp(request)
    }
}
