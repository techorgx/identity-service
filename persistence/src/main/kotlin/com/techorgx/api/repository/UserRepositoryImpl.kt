package com.techorgx.api.repository

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue
import com.techorgx.api.entity.User
import com.techorgx.api.util.UserStatus
import io.grpc.Status
import io.grpc.StatusException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component

@Component
class UserRepositoryImpl(
    private val dynamoDBMapper: DynamoDBMapper
) : UserRepository {
    override fun <S : User?> save(entity: S): S {
        if (entity == null) {
            logger.error("method = $SAVE, Provided entity is null")
            throw StatusException(Status.INTERNAL.withDescription("Internal error"))
        }
        val saveExpression = DynamoDBSaveExpression()
        saveExpression.expected = mapOf(
            HASH_KEY_ATTRIBUTE to ExpectedAttributeValue(false)
        )
        try {
            dynamoDBMapper.save(entity, saveExpression)
            logger.info("method = $SAVE, User saved successfully ${entity.username}")
        } catch (e: ConditionalCheckFailedException) {
            logger.error("method = $SAVE, User already exists with username ${entity.username}")
            throw StatusException(Status.INTERNAL.withDescription("${entity.username} already exists"))
        } catch (e: Exception) {
            logger.error(e)
            throw StatusException(Status.INTERNAL.withDescription("${entity.username} can not be saved"))
        }

        return entity
    }

    override fun findById(id: String): User? {
        try {
            return dynamoDBMapper.load(User::class.java, id)
        } catch (e: Exception) {
            logger.error("method = $FIND_BY_ID, error = $e")
        }
        return null
    }

    override fun updateUserStatus(
        id: String,
        status: UserStatus,
    ) {
        val user = findById(id)
        user?.let {
            it.userStatus = status.toString()
            save(user)
        } ?: run {
            logger.error("method = $UPDATE_USER_STATUS, error = User does not exists")
            throw StatusException(Status.INTERNAL.withDescription("User can not be updated"))
        }
    }

    private companion object {
        private val logger: Logger = LogManager.getLogger(UserRepositoryImpl::class.java)
        private const val FIND_BY_ID = "findById"
        private const val SAVE = "save"
        private const val UPDATE_USER_STATUS = "updateUserStatus"
        private const val HASH_KEY_ATTRIBUTE = "username"
    }
}
