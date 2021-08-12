package com.notnotme.brewdogdiy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Temp type representation
 */
@Parcelize
data class Temp(
    @SerializedName("temp") val temp: Value?,
    @SerializedName("duration") val duration: Int?,
) : Parcelable
