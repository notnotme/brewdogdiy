package com.notnotme.brewdogdiy.repository.datastore

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.model.domain.DownloadStatus

/**
 * A data source capable of doing local request for beers
 */
@Database(
    version = 1,
    entities = [
        DownloadStatus::class,
        Beer::class
    ]
)
@TypeConverters(DateConverter::class)
abstract class BeerDataStore : RoomDatabase() {

    /** @see com.notnotme.brewdogdiy.repository.datastore.BeerDao */
    abstract fun beerDao(): BeerDao

    /** @see com.notnotme.brewdogdiy.repository.datastore.UpdateDao */
    abstract fun updateDao(): UpdateDao

}
