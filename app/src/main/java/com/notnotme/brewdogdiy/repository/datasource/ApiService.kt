package com.notnotme.brewdogdiy.repository.datasource

import com.notnotme.brewdogdiy.model.BeerList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * An interface for the PunkAPI web service
 */
interface ApiService {

    /**
     * @param page The starting page of the results
     * @param perPage The wanted number of results per page
     * @return A list of beers starting from page page containing a maximum of perPage results.
     */
    @GET("/v2/beers")
    suspend fun getBeers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response<BeerList>

    /**
     * @return The beer with the corresponding id
     */
    @GET("/v2/beers/{id}")
    suspend fun getBeer(
        @Path("id") id: Long
    ): Response<BeerList>

    /**
     * @return A random beer
     */
    @GET("/v2/beers/random")
    suspend fun getRandomBeer(): Response<BeerList>

}