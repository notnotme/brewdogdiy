package com.notnotme.brewdogdiy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Beer type representation
 */
@Parcelize
data class Beer(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("tagline") val tagLine: String?,
    @SerializedName("first_brewed") val firstBrewed: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("abv") val abv: Float,
    @SerializedName("ibu") val ibu: Float,
    @SerializedName("target_fg") val targetFg: Float,
    @SerializedName("target_og") val targetOg: Float,
    @SerializedName("ebc") val ebc: Float,
    @SerializedName("srm") val srm: Float,
    @SerializedName("ph") val ph: Float,
    @SerializedName("attenuation_level") val attenuationLevel: Float,
    @SerializedName("volume") val volume: Value?,
    @SerializedName("boil_volume") val boilVolume: Value?,
    @SerializedName("method") val method: Method?,
    @SerializedName("ingredients") val ingredients: Ingredients?,
    @SerializedName("food_pairing") val foodPairing: List<String>?,
    @SerializedName("brewers_tips") val brewersTips: String?,
    @SerializedName("contributed_by") val contributedBy: String?
) : Parcelable
