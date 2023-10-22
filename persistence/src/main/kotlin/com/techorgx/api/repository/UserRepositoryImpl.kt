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
    private val dynamoDBMapper: DynamoDBMapper,
) : UserRepository {
    override fun <S : User?> save(entity: S): S {
        if (entity == null) {
            logger.error("method = $SAVE, message = Provided entity is null")
            throw StatusException(Status.INTERNAL.withDescription("Internal error"))
        }
        val saveExpression = DynamoDBSaveExpression()
        saveExpression.expected =
            mapOf(
                HASH_KEY_ATTRIBUTE to ExpectedAttributeValue(false),
            )
        try {
            dynamoDBMapper.save(entity, saveExpression)
            logger.info("method = $SAVE, message = User saved successfully ${entity.username}")
        } catch (e: ConditionalCheckFailedException) {
            logger.error("method = $SAVE, message = User already exists with username ${entity.username}")
            throw e
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
            logger.error("method = $FIND_BY_ID, message = $e")
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
            dynamoDBMapper.save(user)
        } ?: run {
            logger.error("method = $UPDATE_USER_STATUS, message = User does not exists")
            throw StatusException(Status.INTERNAL.withDescription("User can not be updated"))
        }
    }

    override fun deleteUser(username: String) {
        try {
            val user = User()
            user.username = username
            dynamoDBMapper.delete(user)
            logger.info("method = $DELETE_USER, message = User delete successfully")
        } catch (e: Exception) {
            logger.error("method = $DELETE_USER, message = User can not be deleted, exception = $e")
        }
    }

    private companion object {
        private val logger: Logger = LogManager.getLogger(UserRepositoryImpl::class.java)
        private const val FIND_BY_ID = "findById"
        private const val SAVE = "save"
        private const val UPDATE_USER_STATUS = "updateUserStatus"
        private const val HASH_KEY_ATTRIBUTE = "username"
        private const val DELETE_USER = "deleteUser"
    }
}
