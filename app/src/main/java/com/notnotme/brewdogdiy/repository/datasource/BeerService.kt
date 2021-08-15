package com.notnotme.brewdogdiy.repository.datasource

import androidx.annotation.IntRange
import com.notnotme.brewdogdiy.model.remote.Beer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface for the PunkAPI web service
 */
interface BeerService {

    /**
     * @param page The starting page of the results
     * @param perPage The wanted number of results per page
     * @return A list of beers starting from page page containing a maximum of perPage results.
     */
    @GET("/v2/beers")
    suspend fun getBeers(
        @Query("page") page: Int,
        @Query("per_page") @IntRange(from = 25, to = 80) perPage: Int
    ): Response<List<Beer>>

}