package com.notnotme.brewdogdiy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.repository.BeerRepository
import com.notnotme.brewdogdiy.repository.datasource.BeerDataSource
import com.notnotme.brewdogdiy.repository.datasource.BeerPagingSource
import com.notnotme.brewdogdiy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * ViewModel for List screen.
 * Featuring a pager to obtain  a paged beer result
 * @param beerRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor(
    private val beerRepository: BeerRepository,
    private val beerDataSource: BeerDataSource
) : ViewModel() {

    companion object {
        const val TAG = "MainActivityViewModel"
        const val PAGE_SIZE = 25
    }

    val beerPager = Pager(
        config = PagingConfig(
            initialLoadSize = PAGE_SIZE * 2,
            pageSize = PAGE_SIZE,
            enablePlaceholders = true,
            jumpThreshold = PAGE_SIZE
        ),
        remoteMediator = null,
        pagingSourceFactory = { BeerPagingSource(beerDataSource) },
    ).flow.cachedIn(viewModelScope)

    /**
     * @param beerId A beer ID, 0, or null
     * @return A beer by it's ID or a random beer if id is null or equals 0
     */
    fun getBeerOrRandom(beerId: Long?) = when (beerId) {
        0L, null -> getRandomBeer()
        else -> getBeer(beerId)
    }

    /**
     * Get a random beer from the backend API and produce a Resource<Beer>
     * @return A producer of Resource<Beer>
     */
    private fun getRandomBeer() = channelFlow<Resource<Beer>> {
        channel.send(Resource.loading(null))
        beerRepository.getRandomBeer().collectLatest {
            channel.send(Resource.success(it))
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO)

    /**
     * Get a beer from by ID
     * @return A producer of Resource<Beer>
     */
    private fun getBeer(beerId: Long) = channelFlow<Resource<Beer>> {
        channel.send(Resource.loading(null))
        beerRepository.getBeer(beerId).collectLatest {
            channel.send(Resource.success(it))
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO)

}