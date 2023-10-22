package com.techorgx.api.service

import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException
import com.techorgx.api.mapper.UserMapper
import com.techorgx.api.repository.UserRepository
import com.techorgx.api.validation.ValidationService
import com.techorgx.identity.api.v1.CreateUserRequest
import com.techorgx.identity.api.v1.CreateUserResponse
import io.grpc.Status
import io.grpc.StatusRuntimeException
import org.springframework.stereotype.Service

@Service
class IdentityService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
    private val validationService: ValidationService,
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
}
