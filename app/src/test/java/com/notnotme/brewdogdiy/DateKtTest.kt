package com.notnotme.brewdogdiy

import com.notnotme.brewdogdiy.util.toLocalDate
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class DateKtTest {

    @Test
    fun date_to_string_test() {
        val calendar = Calendar.getInstance(Locale.FRENCH)
        calendar.set(2021, 10, 4, 12, 0)

        val date = calendar.time
        assertNotNull(date.toLocalDate())
    }

}
