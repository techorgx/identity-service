package com.techorgx.api.mapper

import com.techorgx.api.authentication.TokenService
import com.techorgx.api.entity.User
import com.techorgx.identity.api.v1.CreateUserRequest
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val tokenService: TokenService,
) {
    fun mapToUser(request: CreateUserRequest): User {
        return User(
            username = request.username,
            password = tokenService.hashPassword(request.password),
            email = request.email,
        )
    }
}
