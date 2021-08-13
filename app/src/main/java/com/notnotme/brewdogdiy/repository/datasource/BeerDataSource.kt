package com.notnotme.brewdogdiy.repository.datasource

import javax.inject.Inject

/**
 * A data source capable of doing network request against the Punk API
 *
 * @param beerService An instance of BeerService
 */
class BeerDataSource @Inject constructor(
    private val beerService: BeerService
) {

    /** @see com.notnotme.brewdogdiy.repository.datasource.BeerService.getBeers **/
    suspend fun getBeers(page: Int, perPage: Int) = beerService.getBeers(page, perPage)

    /** @see com.notnotme.brewdogdiy.repository.datasource.BeerService.getBeer **/
    suspend fun getBeer(id: Long) = beerService.getBeer(id)

    /** @see com.notnotme.brewdogdiy.repository.datasource.BeerService.getRandomBeer **/
    suspend fun getRandomBeer() = beerService.getRandomBeer()

}