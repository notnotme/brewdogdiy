package com.notnotme.brewdogdiy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Ingredients type representation
 */
@Parcelize
data class Ingredients(
    @SerializedName("malt") val malt: List<Malt>?,
    @SerializedName("hops") val hops: List<Hops>?,
    @SerializedName("yeast") val yeast: String?
) : Parcelable
