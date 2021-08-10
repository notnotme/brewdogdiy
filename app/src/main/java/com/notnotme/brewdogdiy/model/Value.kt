package com.notnotme.brewdogdiy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Value type representation
 */
@Parcelize
data class Value(
    @SerializedName("value")    val value: Float,
    @SerializedName("unit")     val unit: String?
): Parcelable
