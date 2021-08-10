package com.notnotme.brewdogdiy

import com.notnotme.brewdogdiy.util.StringKt
import com.notnotme.brewdogdiy.util.StringKt.parse
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class StringKtTest {

    private val datePatterns = StringKt.commonPatterns

    @Test
    fun test_parse_string_fail() {
        val date = "fail".parse(datePatterns)
        assertNull(date)
    }

    @Test
    fun test_parse_string_yyyy() {
        val date = "2018".parse(datePatterns)
        assertNotNull(date)

        val cal = Calendar.getInstance()
        cal.time = date!!
        assertEquals(cal.get(Calendar.YEAR), 2018)
    }

    @Test
    fun test_parse_string_MM_yyyy() {
        val date = "04-2021".parse(datePatterns)
        assertNotNull(date)

        val cal = Calendar.getInstance()
        cal.time = date!!
        assertEquals(cal.get(Calendar.YEAR), 2021)
    }

}