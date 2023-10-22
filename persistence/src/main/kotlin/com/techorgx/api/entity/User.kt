package com.techorgx.api.entity

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.techorgx.api.util.UserStatus
import java.time.ZoneOffset
import java.time.ZonedDateTime

@DynamoDBTable(tableName = "User")
data class User(
    @get:DynamoDBHashKey(attributeName = "username")
    var username: String = "",
    var userId: String = "",
    var password: String = "",
    var email: String = "",
    var registrationData: String = ZonedDateTime.now(ZoneOffset.UTC).toString(),
    var lastLoginDate: String = ZonedDateTime.now(ZoneOffset.UTC).toString(),
    var userStatus: String = UserStatus.SUSPENDED.toString(),
    var isEmailVerified: Boolean = false,
)
