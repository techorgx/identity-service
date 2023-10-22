package com.techorgx.api.repository

import com.techorgx.api.entity.User
import com.techorgx.api.util.UserStatus

interface UserRepository {
    fun <S : User?> save(entity: S): S

    fun findById(id: String): User?

    fun updateUserStatus(
        id: String,
        status: UserStatus,
    )

    fun deleteUser(username: String)
}
