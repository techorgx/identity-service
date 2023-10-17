package com.techorgx.api.service

import com.techorgx.api.mapper.CustomerMapper
import com.techorgx.api.mapper.UserMapper
import com.techorgx.identity.api.v1.CreateUserRequest
import com.techorgx.identity.api.v1.CreateUserResponse
import org.springframework.stereotype.Service

@Service
class IdentityService(
    private val customerService: CustomerService,
    private val customerMapper: CustomerMapper,
    private val userMapper: UserMapper,
) {
    fun createUser(request: CreateUserRequest): CreateUserResponse {
        val user = userMapper.mapToUser(request)
        val customer = customerMapper.mapToCustomer(user, request)
        customerService.createCustomer(customer)
        return CreateUserResponse.getDefaultInstance()
    }
}
