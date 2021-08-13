package com.notnotme.brewdogdiy.repository.datastore

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.notnotme.brewdogdiy.model.domain.Beer
import kotlinx.coroutines.flow.Flow

/**
 * An interface DAO for beers
 */
@Dao
interface BeerDao {

    /**
     * @return A PagingSource of all Beers in the database
     */
    @Query("SELECT * FROM Beer ORDER BY id ASC")
    fun getBeers(): PagingSource<Int, Beer>

    /**
     * @param id ID of the beer to request
     * @return A Beer with the corresponding id in the database, or null
     */
    @Query("SELECT * FROM Beer WHERE id = :id")
    fun getBeer(id: Long): Flow<Beer?>

    /**
     * Insert a beer into the database
     * @param beer The Beer  to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBeer(beer: Beer)

    /**
     * Insert a list of beers into the database
     * @param beers The list to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBeers(beers: List<Beer>)

}