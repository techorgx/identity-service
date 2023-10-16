package com.techorgx.api.utility

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

@Component
class LocalSecretFileReader(
    @param:Value("\${security.token.keyFilePath}") private val keyFilePath: String,
    private val environment: Environment,
) {
    @Throws(IOException::class)
    fun readSecretKey(): String? {
        val activeProfiles = environment.activeProfiles
        return if (listOf(*activeProfiles).contains("local")) {
            readSecretKeyFromFile()
        } else {
            null
        }
    }

    @Throws(IOException::class)
    private fun readSecretKeyFromFile(): String {
        val path = Paths.get(keyFilePath)
        val secretKeyBytes = Files.readAllBytes(path)
        return String(secretKeyBytes, StandardCharsets.UTF_8)
    }
}
