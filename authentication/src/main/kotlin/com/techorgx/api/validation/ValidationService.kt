package com.techorgx.api.validation

import org.springframework.stereotype.Service
import java.util.regex.Matcher
import java.util.regex.Pattern

@Service
class ValidationService {
    fun validateEmail(email: String): Boolean {
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    companion object {
        private const val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        private val pattern: Pattern = Pattern.compile(EMAIL_PATTERN)
    }
}