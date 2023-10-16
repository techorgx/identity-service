package com.techorgx.api.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.techorgx.api.utility.LocalSecretFileReader
import io.fusionauth.jwt.Signer
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.rsa.RSASigner
import org.bson.types.ObjectId
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Component
class AuthenticationService(
    private val objectMapper: ObjectMapper,
    private val localSecretFileReader: LocalSecretFileReader,
) {
    fun generateJwtToken(claims: Map<String, String>): String {
        val secretKey = localSecretFileReader.readSecretKey()

        val signer: Signer = RSASigner.newSHA256Signer(secretKey)

        val jwt = JWT().setSubject(objectMapper.writeValueAsString(claims))
            .setIssuer(ISSUER)
            .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
            .setAudience("")
            .setUniqueId(ObjectId().toString())
            .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(JWT_TTL_MIN))

        return JWT.getEncoder().encode(jwt, signer)
    }

    private companion object {
        const val ISSUER = "identity-service"
        const val JWT_TTL_MIN = 1L
    }
}
