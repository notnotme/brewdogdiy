package com.notnotme.brewdogdiy.repository.datastore

import androidx.room.*
import com.notnotme.brewdogdiy.model.domain.DownloadStatus

/**
 * An interface DAO for storing update informations related to
 * the remote beers
 */
@Dao
interface UpdateDao {

    /**
     * Get the last saved DownloadStatus, or null
     * @param The id of the DownloadStatus to get
     */
    @Query("SELECT * FROM DownloadStatus WHERE id = :id")
    suspend fun getDownloadStatus(id: Long): DownloadStatus?

    /**
     * Save a DownloadStatus
     * @param downloadStatus the DownloadStatus to save
     * @return The id of the saved DownloadStatus
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDownloadStatus(downloadStatus: DownloadStatus): Long

    /**
     * Delete a DownloadStatus from the database
     * @param The id of the DownloadStatus to get
     * @return The number of deleted items in database
     */
    @Query("DELETE FROM DownloadStatus WHERE id = :id")
    suspend fun deleteDownloadStatus(id: Long): Int

}