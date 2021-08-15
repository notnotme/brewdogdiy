package com.notnotme.brewdogdiy.model.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Remote Hops type representation
 */
@Parcelize
data class Hops(
    @SerializedName("name") val name: String?,
    @SerializedName("amount") val amount: Value?,
    @SerializedName("add") val add: String?,
    @SerializedName("attribute") val attribute: String?
) : Parcelable
