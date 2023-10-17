package com.techorgx.api.mapper

import com.techorgx.api.model.Customer
import com.techorgx.api.model.User
import com.techorgx.identity.api.v1.CreateUserRequest
import org.springframework.stereotype.Component

@Component
class CustomerMapper {
    fun mapToCustomer(
        user: User,
        request: CreateUserRequest,
    ): Customer {
        return Customer(
            id = user.userId,
            firstName = request.firstName,
            lastName = request.lastName,
            address = "",
            city = "",
            pincode = "",
            email = request.email,
        )
    }
}
