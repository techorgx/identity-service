package com.techorgx.api.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.techorgx.api.model.OpaqueToken
import com.techorgx.api.utility.LocalSecretFileReader
import com.techorgx.api.utility.MetadataUtils
import com.techorgx.identity.api.v1.LoginUserRequest
import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.rsa.RSASigner
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID

@Component
class TokenService(
    @Value("\${security.token.audience}")
    private val audience: List<String>,
    @Value("\${security.token.jwtTtlMin}")
    private val jwtTtlMin: Long,
    @Value("\${security.token.opaqueTokenTtlHrs}")
    private val opaqueTokenTtlHrs: Long,
    private val objectMapper: ObjectMapper,
    private val localSecretFileReader: LocalSecretFileReader,
    private val metadataUtils: MetadataUtils,
) {
    fun generateJwtToken(claims: Map<String, String>): String {
        val secretKey = localSecretFileReader.readSecretKey()

        val signer: Signer = RSASigner.newSHA256Signer(secretKey)

        val jwt =
            JWT().setSubject(objectMapper.writeValueAsString(claims))
                .setIssuer(ISSUER)
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setAudience(audience)
                .setUniqueId(ObjectId().toString())
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(jwtTtlMin))

        return JWT.getEncoder().encode(jwt, signer)
    }

    fun generateOpaqueToken(request: LoginUserRequest): OpaqueToken {
        return OpaqueToken(
            tokenId = UUID.randomUUID().toString(),
            username = request.username,
            expirationTimestamp = ZonedDateTime.now(ZoneOffset.UTC).plusHours(opaqueTokenTtlHrs),
            locale = metadataUtils.getLocale(),
            deviceId = metadataUtils.getDeviceId(),
        )
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    private companion object {
        const val ISSUER = "identity-service"
    }
}
