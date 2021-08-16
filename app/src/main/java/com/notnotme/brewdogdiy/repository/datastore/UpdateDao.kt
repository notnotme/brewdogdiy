package com.notnotme.brewdogdiy.repository.datastore

import androidx.room.*
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import kotlinx.coroutines.flow.Flow

/**
 * An interface DAO for storing update informations related to
 * the remote beers
 */
@Dao
interface UpdateDao {

    /**
     * @return The saved DownloadStatus, or null
     */
    @Query("SELECT * FROM DownloadStatus WHERE id = 1")
    fun getDownloadStatus(): Flow<DownloadStatus?>

    /**
     * Save a DownloadStatus
     * @param downloadStatus the DownloadStatus to save
     * @return The id of the saved DownloadStatus (should be 1L)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDownloadStatus(downloadStatus: DownloadStatus): Long

    /**
     * Delete all DownloadStatus from the database
     * @return The number of deleted items in database (should be 0 or 1)
     */
    @Query("DELETE FROM DownloadStatus")
    suspend fun deleteDownloadStatus(): Int

}