package com.notnotme.brewdogdiy.util

import java.text.SimpleDateFormat
import java.util.*

object StringKt {

    val commonPatterns = listOf("MM-yyyy", "yyyy")

    fun String.parse(patterns: List<String>): Date? {
        for (pattern in patterns) {
            try {
                return SimpleDateFormat(pattern, Locale.US).parse(this)
            } catch (_: Exception) { }
        }

        return null
    }

}
