package com.notnotme.brewdogdiy.model.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Remote Method type representation
 */
@Parcelize
data class Method(
    @SerializedName("mash_temp") val temp: List<Temp>?,
    @SerializedName("fermentation") val fermentation: Temp?,
    @SerializedName("twist") val twist: String?
) : Parcelable
