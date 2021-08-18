package com.notnotme.brewdogdiy.util

import java.text.SimpleDateFormat
import java.util.*

private val dateFormat by lazy { SimpleDateFormat.getDateTimeInstance() }
private val calendar by lazy { Calendar.getInstance() }

fun Date.toLocalDate(): String = dateFormat.format(this)

fun Date.getYearAsString() = calendar.let {
    it.time = this
    it.get(Calendar.YEAR).toString()
}
