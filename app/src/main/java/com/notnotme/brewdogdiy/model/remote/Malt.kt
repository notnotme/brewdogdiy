package com.notnotme.brewdogdiy.model.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Remote Malt type representation
 */
@Parcelize
data class Malt(
    @SerializedName("name") val name: String?,
    @SerializedName("amount") val amount: Value?
) : Parcelable
