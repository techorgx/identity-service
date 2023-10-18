package com.techorgx.api.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.techorgx.api.model.User
import com.techorgx.api.utility.UserStatus
import io.grpc.Status
import io.grpc.StatusException
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(
    private val dynamoDBMapper: DynamoDBMapper,
) : UserRepository {
    override fun <S : User?> save(entity: S): S {
        try {
            dynamoDBMapper.save(entity)
        } catch (e: Exception) {
            logger.error(e)
            throw StatusException(Status.INTERNAL.withDescription("User can not be saved"))
        }
        return entity
    }

    override fun findById(id: String): User? {
        try {
            return dynamoDBMapper.load(User::class.java, id)
        } catch (e: Exception) {
            logger.error(e)
        }
        return null
    }

    override fun updateUserStatus(
        id: String,
        status: UserStatus,
    ) {
        val user = findById(id)
        user?.let {
            it.userStatus = status
            save(user)
        } ?: throw StatusException(Status.INTERNAL.withDescription("User can not be updated"))
    }

    private companion object {
        val logger = LogManager.getLogger(UserRepositoryImpl::class.java)
    }
}
