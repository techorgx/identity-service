package com.techorgx.api.entity

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.techorgx.api.util.UserStatus
import java.time.ZoneOffset
import java.time.ZonedDateTime

@DynamoDBTable(tableName = "User")
data class User(
    @get:DynamoDBHashKey(attributeName = "username")
    var username: String = "",

    @get:DynamoDBRangeKey(attributeName = "userId")
    var userId: String = "",

    var password: String = "",
    var email: String = "",

    @get:DynamoDBAttribute(attributeName = "registrationData")
    var registrationData: String = ZonedDateTime.now(ZoneOffset.UTC).toString(),

    @get:DynamoDBAttribute(attributeName = "lastLoginDate")
    var lastLoginDate: String = ZonedDateTime.now(ZoneOffset.UTC).toString(),

    @get:DynamoDBAttribute(attributeName = "userStatus")
    var userStatus: String = UserStatus.SUSPENDED.toString(),

    @get:DynamoDBAttribute(attributeName = "isEmailVerified")
    var isEmailVerified: Boolean = false
)
