package com.techorgx.api.service

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException
import com.techorgx.api.authentication.TokenService
import com.techorgx.api.cache.CacheService
import com.techorgx.api.mapper.UserMapper
import com.techorgx.api.repository.UserRepository
import com.techorgx.api.validation.ValidationService
import com.techorgx.identity.api.v1.CreateUserRequest
import com.techorgx.identity.api.v1.CreateUserResponse
import com.techorgx.identity.api.v1.GenerateJwtRequest
import com.techorgx.identity.api.v1.GenerateJwtResponse
import com.techorgx.identity.api.v1.LoginUserRequest
import com.techorgx.identity.api.v1.LoginUserResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.springframework.stereotype.Service

@Service
class IdentityService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val validationService: ValidationService,
    private val tokenService: TokenService,
    private val cacheService: CacheService,
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
            val authenticated = validationService.validatePassword(request.password, user.password)
            if (authenticated) {
                val opaqueToken = tokenService.generateOpaqueToken(request)
                cacheService.cache.put(opaqueToken.tokenId, opaqueToken)
                return buildLoginUserResponse(
                    username = request.username,
                    isAuthenticated = true,
                    userExists = true,
                    opaqueToken = opaqueToken.tokenId,
                )
            } else {
                return buildLoginUserResponse(username = request.username, isAuthenticated = false, userExists = true, opaqueToken = "")
            }
        }
        return buildLoginUserResponse(username = request.username, isAuthenticated = false, userExists = false, opaqueToken = "")
    }

    fun generateJwt(request: GenerateJwtRequest): GenerateJwtResponse {
        val opaqueToken = cacheService.cache.getIfPresent(request.tokenId)
        opaqueToken?.let {
            val jwt = tokenService.generateJwtToken(emptyMap())
            return GenerateJwtResponse
                .newBuilder()
                .setJwtToken(jwt)
                .build()
        }
        return GenerateJwtResponse
            .newBuilder()
            .setJwtToken("")
            .build()
    }

    private fun buildLoginUserResponse(
        username: String,
        isAuthenticated: Boolean,
        userExists: Boolean,
        opaqueToken: String,
    ): LoginUserResponse {
        val responseBuilder = LoginUserResponse.newBuilder()
        responseBuilder.isAuthenticated = isAuthenticated
        responseBuilder.username = username
        responseBuilder.userExists = userExists
        responseBuilder.opaqueToken = opaqueToken
        return responseBuilder.build()
    }
}
