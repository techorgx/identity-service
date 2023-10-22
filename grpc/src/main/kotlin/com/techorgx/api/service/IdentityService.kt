package com.techorgx.api.service

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException
import com.techorgx.api.authentication.TokenService
import com.techorgx.api.mapper.UserMapper
import com.techorgx.api.repository.UserRepository
import com.techorgx.api.validation.ValidationService
import com.techorgx.identity.api.v1.CreateUserRequest
import com.techorgx.identity.api.v1.CreateUserResponse
import com.techorgx.identity.api.v1.LoginUserRequest
import com.techorgx.identity.api.v1.LoginUserResponse
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.StatusRuntimeException
import org.springframework.stereotype.Service

@Service
class IdentityService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val validationService: ValidationService,
    private val tokenService: TokenService,
) {
    fun createUser(request: CreateUserRequest): CreateUserResponse {
        val user = userMapper.mapToUser(request)
        if (!validationService.validateEmail(user.email)) {
            throw StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription("Invalid email"))
        }
        var doesUserExists = false
        try {
            userRepository.save(user)
        } catch (e: ConditionalCheckFailedException) {
            doesUserExists = true
        }

        return CreateUserResponse.newBuilder()
            .setUsername(user.username)
            .setUserStatus(user.userStatus)
            .setIsEmailVerified(user.isEmailVerified)
            .setUserExists(doesUserExists)
            .build()
    }

    fun loginUser(request: LoginUserRequest): LoginUserResponse {
        val user = userRepository.findById(request.username)
        user?.let {
            val responseBuilder = LoginUserResponse.newBuilder()
            val authenticated = validationService.validatePassword(request.password, user.password)
            if (authenticated) {
                responseBuilder.isAuthenticated = true
                responseBuilder.username = request.username
                val opaqueToken = tokenService.generateOpaqueToken(request)
                responseBuilder.opaqueToken = opaqueToken.tokenId
                // save opaque token
            } else {
                responseBuilder.username = request.username
                responseBuilder.isAuthenticated = false
                return responseBuilder.build()
            }
            return responseBuilder.build()
        }
        throw StatusException(Status.UNAUTHENTICATED.withDescription("User not found"))
    }
}
