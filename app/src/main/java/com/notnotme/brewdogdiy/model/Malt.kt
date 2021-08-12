package com.notnotme.brewdogdiy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Malt type representation
 */
@Parcelize
data class Malt(
    @SerializedName("name") val name: String?,
    @SerializedName("amount") val amount: Value?
) : Parcelable
