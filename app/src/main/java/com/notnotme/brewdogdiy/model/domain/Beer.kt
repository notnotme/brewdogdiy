package com.notnotme.brewdogdiy.model.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Local Beer type representation
 */
@Entity
@Parcelize
data class Beer(
    @PrimaryKey val id: Long,
    val name: String,
    val tagLine: String,
    val firstBrewed: Date?,
    val description: String,
    val imageUrl: String?,
    val abv: Float,
    val contributedBy: String
) : Parcelable
