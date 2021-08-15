package com.notnotme.brewdogdiy.model.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Remote Temp type representation
 */
@Parcelize
data class Temp(
    @SerializedName("temp") val temp: Value?,
    @SerializedName("duration") val duration: Int?,
) : Parcelable
