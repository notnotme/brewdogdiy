package com.notnotme.brewdogdiy.model.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * DownloadStatus type representation
 * @param id The id
 * @param lastUpdate The last update time
 * @param totalBeers The current total of beers in database
 * @param page The last page that was downloading (resume from it and not the next if isFinished == false)
 * @param isFinished True if download is finished, false otherwise
 */
@Entity
@Parcelize
data class DownloadStatus(
    @PrimaryKey val id: Long,
    var lastUpdate: Date,
    var totalBeers: Int,
    var page: Int,
    var isFinished: Boolean
) : Parcelable
