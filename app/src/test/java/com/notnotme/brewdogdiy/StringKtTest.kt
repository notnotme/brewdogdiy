package com.notnotme.brewdogdiy

import com.notnotme.brewdogdiy.util.StringKt.toDate
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class StringKtTest {

    @Test
    fun test_parse_string_fail() {
        val date = "fail".toDate()
        assertNull(date)
    }

    @Test
    fun test_parse_string_yyyy() {
        val date = "2018".toDate()
        assertNotNull(date)

        val cal = Calendar.getInstance()
        cal.time = date!!
        assertEquals(cal.get(Calendar.YEAR), 2018)
    }

    @Test
    fun test_parse_string_MMyyyy() {
        val date = "04/2021".toDate()
        assertNotNull(date)

        val cal = Calendar.getInstance()
        cal.time = date!!
        assertEquals(cal.get(Calendar.YEAR), 2021)
        assertEquals(cal.get(Calendar.MONTH), Calendar.APRIL)
    }

    @Test
    fun test_parse_string_MM_yyyy() {
        val date = "04-2021".toDate()
        assertNull(date)
    }

}