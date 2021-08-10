package com.notnotme.brewdogdiy.repository

import javax.inject.Inject

/**
 * A data source capable of doing network request against the Punk API
 *
 * @param apiService An implementation of ApiService
 */
class ApiDataSource @Inject constructor(
    private val apiService: ApiService
) {

    /** @see com.notnotme.brewdogdiy.repository.ApiService.getBeers **/
    suspend fun getBeers(page: Int, perPage: Int) = apiService.getBeers(page, perPage)

    /** @see com.notnotme.brewdogdiy.repository.ApiService.getBeer **/
    suspend fun getBeer(id: Long) = apiService.getBeer(id)

    /** @see com.notnotme.brewdogdiy.repository.ApiService.getRandomBeer **/
    suspend fun getRandomBeer() = apiService.getRandomBeer()

}