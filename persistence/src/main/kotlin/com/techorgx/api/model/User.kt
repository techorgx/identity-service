package com.techorgx.api.model

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.techorgx.api.utility.UserStatus
import java.time.ZoneOffset
import java.time.ZonedDateTime

@DynamoDBTable(tableName = "User")
data class User(
    @DynamoDBHashKey
    val userId: String = "",
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val registrationData: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC),
    var lastLoginDate: ZonedDateTime = ZonedDateTime.now(ZoneOffset.UTC),
    var userStatus: UserStatus = UserStatus.SUSPENDED,
    var isEmailVerified: Boolean = false,
)
