package com.techorgx.api.model

import com.techorgx.api.utility.UserStatus
import java.time.ZoneOffset
import java.time.ZonedDateTime

data class User(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val userId: String = "",
    val registrationData: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC),
    val lastLoginDate: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC),
    val userStatus: UserStatus = UserStatus.SUSPENDED,
    val isEmailVerified: Boolean = false,
)
