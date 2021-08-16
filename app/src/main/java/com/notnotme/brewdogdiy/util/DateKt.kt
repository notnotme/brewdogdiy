package com.notnotme.brewdogdiy.util

import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat.getDateTimeInstance()

fun Date.toLocalDate(): String = dateFormat.format(this)
