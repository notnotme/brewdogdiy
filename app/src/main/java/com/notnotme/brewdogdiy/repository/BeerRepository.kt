package com.notnotme.brewdogdiy.repository

import android.util.Log
import androidx.room.withTransaction
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.repository.datasource.BeerDataSource
import com.notnotme.brewdogdiy.repository.datastore.BeerDataStore
import com.notnotme.brewdogdiy.util.contentOrNull
import com.notnotme.brewdogdiy.util.toDate
import javax.inject.Inject
import com.notnotme.brewdogdiy.model.domain.Beer as DomainBeer
import com.notnotme.brewdogdiy.model.remote.Beer as RemoteBeer

/**
 * A repository that provide alcohol.
 *
 * @param beerDataSource An instance of ApiDataSource
 * @param beerDataStore An instance of BeerDataStore
 */
class BeerRepository @Inject constructor(
    private val beerDataSource: BeerDataSource,
    private val beerDataStore: BeerDataStore
) {

    companion object {
        const val TAG = "BeerRepository"
    }

    private val beerDao = beerDataStore.beerDao()
    private val updateDao = beerDataStore.updateDao()

    /** Run block of code inside a transaction */
    suspend fun runInTransaction(block: suspend () -> Unit) = beerDataStore.withTransaction {
        block()
    }

    /** @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.getBeers */
    fun getBeersFromDao() = beerDao.getBeers()

    /** @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.getBeer */
    fun getBeerFromDao(id: Long) = beerDao.getBeer(id)

    /** @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.getRandomBeer */
    fun getRandomBeerFromDao() = beerDao.getRandomBeer()

    /** @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.getBeersByAbv */
    fun getBeersByAbvFromDao(min: Float, max: Float) = beerDao.getBeersByAbv(min, max)

    /** @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.getBeersByIbu */
    fun getBeersByIbuFromDao(min: Float, max: Float) = beerDao.getBeersByIbu(min, max)

    /** @see com.notnotme.brewdogdiy.repository.datastore.UpdateDao.getDownloadStatus */
    fun getDownloadStatus() = updateDao.getDownloadStatus()

    /** @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.deleteBeers */
    suspend fun deleteBeers() = beerDao.deleteBeers()

    /**
     * Save a list of RemoteBeer to database, converting them to DomainBeer beforehand.
     * @return The ids of saved items
     * @see com.notnotme.brewdogdiy.repository.datastore.BeerDao.saveBeers
     */
    suspend fun saveBeersToDao(beers: List<RemoteBeer>): List<Long> {
        // First convert all beers that we can
        val domainBeers = mutableListOf<DomainBeer>()
        beers.forEach {
            try {
                domainBeers.add(
                    DomainBeer(
                        id = it.id,
                        name = it.name?.contentOrNull() ?: error("No name for id ${it.id}"),
                        tagLine = it.tagLine?.contentOrNull() ?: error("No tagline for id ${it.id}"),
                        imageUrl = it.imageUrl?.contentOrNull(),
                        abv = it.abv ?: error("No abv for id ${it.id}"),
                        ibu = it.ibu ?: error("No ibu for id ${it.id}"),
                        description = it.description?.contentOrNull() ?: error("No description for id ${it.id}"),
                        firstBrewed = it.firstBrewed?.toDate(),
                        contributedBy = it.contributedBy?.contentOrNull() ?: error("No contributor for id ${it.id}")
                    )
                )
            } catch (exception: Exception) {
                val error = exception.message?.contentOrNull() ?: "Unknown error"
                Log.w(
                    TAG,
                    "Error while converting RemoteBeer to DomainBeer (id: ${it.id}) : $error"
                )
            }
        }

        return beerDao.saveBeers(domainBeers)
    }

    /** @see com.notnotme.brewdogdiy.repository.datastore.UpdateDao.saveDownloadStatus */
    suspend fun saveDownloadStatus(downloadStatus: DownloadStatus) = updateDao.saveDownloadStatus(downloadStatus)

    /** @see com.notnotme.brewdogdiy.repository.datastore.UpdateDao.deleteDownloadStatus */
    suspend fun deleteDownloadStatus() = updateDao.deleteDownloadStatus()

    /** @see com.notnotme.brewdogdiy.repository.datasource.BeerService.getBeers */
    suspend fun getBeersFromRemote(page: Int, perPage: Int) = beerDataSource.getBeers(page, perPage)

}