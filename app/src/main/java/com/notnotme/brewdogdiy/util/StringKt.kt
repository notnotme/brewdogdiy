package com.notnotme.brewdogdiy.util

import org.apache.commons.validator.GenericValidator
import java.text.SimpleDateFormat
import java.util.*

object StringKt {

    private val acceptPatterns by lazy {
        listOf("MM/yyyy", "yyyy")
    }

    private val formatter by lazy {
        SimpleDateFormat("", Locale.US).apply {
            isLenient = true
        }
    }

    /**
     * Try to convert a String to a Date.
     * Only a small subset of patterns are tested against the String.
     * @see acceptPatterns
     * @return null in case of failure, true otherwise
     */
    fun String.toDate(): Date? {
        for (pattern in acceptPatterns) {
            if (GenericValidator.isDate(this, pattern, true)) {
                try {
                    formatter.applyPattern(pattern)
                    return formatter.parse(this)
                } catch (_: Exception) {
                }
            }
        }

        return null
    }

    /**
     * @return The content of String if not blank or null, null otherwise
     */
    fun String.contentOrNull() = if (this.isNotBlank()) this else null

}
