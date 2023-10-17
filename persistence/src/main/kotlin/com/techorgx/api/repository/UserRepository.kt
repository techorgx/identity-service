package com.techorgx.api.repository

import com.techorgx.api.model.User
import com.techorgx.api.utility.UserStatus

interface UserRepository {
    fun <S : User?> save(entity: S): S

    fun findById(id: String): User?

    fun updateUserStatus(
        id: String,
        status: UserStatus,
    )
}
