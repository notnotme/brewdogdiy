package com.notnotme.brewdogdiy.repository

import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.repository.datasource.BeerDataSource
import com.notnotme.brewdogdiy.repository.datastore.BeerDao
import com.notnotme.brewdogdiy.util.StringKt.contentOrNull
import com.notnotme.brewdogdiy.util.StringKt.toDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * A repository that provide alcohol.
 *
 * todo: deduplicate remote beer to domain beer
 *
 * @param beerDataSource An instance of ApiDataSource to request online data source
 * @param beerDao An instance of BeerDao, used to keep online results in cache
 */
@ExperimentalCoroutinesApi
class BeerRepository @Inject constructor(
    private val beerDataSource: BeerDataSource,
    private val beerDao: BeerDao
) {

    /**
     * Get a Beer from local storage (first) or network (last)
     * @param id Id of the Beer
     * @return FlowCollector of a Beer
     * @see com.notnotme.brewdogdiy.repository.datasource.BeerDataSource.getBeer
     * @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.getBeer
     **/
    fun getBeer(id: Long) = channelFlow<Beer> {
        // First check local storage
        beerDao.getBeer(id).collectLatest { beer ->
            if (beer != null) {
                // Found in local storage
                channel.send(beer)
            } else {
                // Not in local storage: request API
                beerDataSource.getBeer(id).let { response ->
                    val body = response.body()
                    if (response.isSuccessful && body != null) {
                        // Got it
                        val remoteBeer = body[0]
                        val domainBeer = Beer(
                            id = remoteBeer.id,
                            name = remoteBeer.name?.contentOrNull() ?: error("No name"),
                            tagLine = remoteBeer.tagLine?.contentOrNull() ?: error("No tagline"),
                            imageUrl = remoteBeer.imageUrl?.contentOrNull(),
                            abv = remoteBeer.abv ?: error("No abv"),
                            description = remoteBeer.description?.contentOrNull() ?: error("No description"),
                            firstBrewed = remoteBeer.firstBrewed?.toDate(),
                            contributedBy = remoteBeer.contributedBy?.contentOrNull() ?: error("No contributor")
                        )

                        // Save this beer in database
                        beerDao.insertBeer(domainBeer)
                        channel.send(domainBeer)
                    } else {
                        error(response.message().contentOrNull() ?: "Unknown error")
                    }
                }
            }
        }
    }
    .flowOn(Dispatchers.IO)

    /**
     * Get a random Beer from network
     * @return FlowCollector of a Beer
     * @see com.notnotme.brewdogdiy.repository.datasource.BeerDataSource.getRandomBeer
     */
    fun getRandomBeer() = channelFlow<Beer> {
        beerDataSource.getRandomBeer().let {
            val body = it.body()
            if (it.isSuccessful && body != null) {
                val beer = body[0]
                val domainBeer = Beer(
                    id = beer.id,
                    name = beer.name?.contentOrNull() ?: error("No name"),
                    tagLine = beer.tagLine?.contentOrNull() ?: error("No tagline"),
                    imageUrl = beer.imageUrl?.contentOrNull(),
                    abv = beer.abv ?: error("No abv"),
                    description = beer.description?.contentOrNull() ?: error("No description"),
                    firstBrewed = beer.firstBrewed?.toDate(),
                    contributedBy = beer.contributedBy?.contentOrNull() ?: error("No contributor")
                )

                // Also insert or update the database beer
                beerDao.insertBeer(domainBeer)
                channel.send(domainBeer)
            } else {
                error(it.message().contentOrNull() ?: "Unknown error")
            }
        }
    }.flowOn(Dispatchers.IO)

}