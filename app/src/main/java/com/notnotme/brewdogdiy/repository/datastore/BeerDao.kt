package com.notnotme.brewdogdiy.repository.datastore

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.notnotme.brewdogdiy.model.domain.Beer
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * An interface DAO for beers
 */
@Dao
interface BeerDao {

    /**
     * @return A PagingSource<Int, Beer> of all Beers stored in the database
     */
    @Query("SELECT * FROM Beer ORDER BY id ASC")
    fun getBeers(): PagingSource<Int, Beer>


    /**
     * @param min The minimum abv of the beers
     * @param max The maximum abv of the beers
     * @param orderByDesc If the result should be order by desc
     * @return A PagingSource<Int, Beer> of all corresponding Beers stored in the database
     */
    @Query("""SELECT * FROM Beer WHERE abv >= :min AND abv <= :max ORDER BY
            CASE WHEN :orderByDesc = 1 THEN abv END DESC,
            CASE WHEN :orderByDesc = 0 THEN abv END ASC""")
    fun getBeersByAbv(
        min: Float,
        max: Float,
        orderByDesc: Boolean
    ): PagingSource<Int, Beer>

    /**
     * @param min The minimum ibu of the beers
     * @param max The maximum ibu of the beers
     * @param orderByDesc If the result should be order by desc
     * @return A PagingSource<Int, Beer> of all corresponding Beers stored in the database
     */
    @Query("""SELECT * FROM Beer WHERE ibu >= :min AND ibu <= :max ORDER BY
            CASE WHEN :orderByDesc = 1 THEN ibu END DESC,
            CASE WHEN :orderByDesc = 0 THEN ibu END ASC""")
    fun getBeersByIbu(
        min: Float,
        max: Float,
        orderByDesc: Boolean
    ): PagingSource<Int, Beer>

    /**
     * @param min The minimum first brewed date of the beers
     * @param max The maximum first brewed date of the beers
     * @param orderByDesc If the result should be order by desc
     * @return A PagingSource<Int, Beer> of all corresponding Beers stored in the database
     */
    @Query("""SELECT * FROM Beer WHERE firstBrewed >= :min AND firstBrewed <= :max ORDER BY
            CASE WHEN :orderByDesc = 1 THEN firstBrewed END DESC,
            CASE WHEN :orderByDesc = 0 THEN firstBrewed END ASC""")
    fun getBeersByBrewDate(
        min: Date,
        max: Date,
        orderByDesc: Boolean
    ): PagingSource<Int, Beer>

    /**
     * @param id ID of the beer to request
     * @return A Flow<Beer>, or null
     */
    @Query("SELECT * FROM Beer WHERE id = :id")
    fun getBeer(id: Long): Flow<Beer?>

    /**
     * @return A random beer that is stored in the database
     */
    @Query("SELECT * FROM Beer ORDER BY RANDOM() LIMIT 1")
    fun getRandomBeer(): Flow<Beer?>

    /**
     * Insert or replace a list of beers into the database
     * @param beers The list to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBeers(beers: List<Beer>): List<Long>

    /**
     * Remove all Beer from the database
     * @return Number of deleted items
     */
    @Query("DELETE FROM Beer")
    suspend fun deleteBeers(): Int

}