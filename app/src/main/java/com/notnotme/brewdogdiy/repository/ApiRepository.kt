package com.notnotme.brewdogdiy.repository

import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * A repository that provide alcohol.
 *
 * @param apiDataSource An instance of ApiDataSource
 */
@ActivityRetainedScoped
class ApiRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
) {

    /**
     * @param page The page to request
     * @param perPage Number of result per page
     * @return FlowCollector of a list of Beers
     * @see com.notnotme.brewdogdiy.repository.ApiDataSource.getBeers
     **/
    suspend fun getBeers(page: Int, perPage: Int) = flow {
        emit(apiDataSource.getBeers(page, perPage))
    }.flowOn(Dispatchers.IO)

    /**
     * @param id Id of the Beer
     * @return FlowCollector of a Beer
     * @see com.notnotme.brewdogdiy.repository.ApiDataSource.getBeer
     **/
    suspend fun getBeer(id: Long) = flow {
        emit(apiDataSource.getBeer(id))
    }.flowOn(Dispatchers.IO)

    /**
     * @return FlowCollector of a Beer
     * @see com.notnotme.brewdogdiy.repository.ApiDataSource.getRandomBeer
     **/
    suspend fun getRandomBeer() = flow {
        emit(apiDataSource.getRandomBeer())
    }.flowOn(Dispatchers.IO)

}