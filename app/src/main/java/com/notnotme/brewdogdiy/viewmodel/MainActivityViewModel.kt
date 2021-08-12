package com.notnotme.brewdogdiy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.notnotme.brewdogdiy.model.Beer
import com.notnotme.brewdogdiy.repository.ApiRepository
import com.notnotme.brewdogdiy.repository.BeerPagingSource
import com.notnotme.brewdogdiy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * ViewModel for List screen.
 * Featuring a pager to obtain  a paged beer result
 * @param apiRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
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
        pagingSourceFactory = { BeerPagingSource(apiRepository) },
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
        apiRepository.getRandomBeer().collectLatest {
            val body = it.body()
            if (!it.isSuccessful || body == null) {
                channel.send(Resource.error(it.message(), null))
            } else {
                channel.send(Resource.success(body[0]))
            }
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, null)

    /**
     * Get a beer from the backend API by Id
     * @return A producer of Resource<Beer>
     */
    private fun getBeer(beerId: Long) = channelFlow<Resource<Beer>> {
        channel.send(Resource.loading(null))
        apiRepository.getBeer(beerId).collectLatest {
            val body = it.body()
            if (!it.isSuccessful || body == null) {
                channel.send(Resource.error(it.message(), null))
            } else {
                channel.send(Resource.success(body[0]))
            }
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Eagerly, null)

}