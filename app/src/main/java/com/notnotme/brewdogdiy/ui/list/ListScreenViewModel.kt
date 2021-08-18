package com.notnotme.brewdogdiy.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.notnotme.brewdogdiy.repository.BeerRepository
import com.notnotme.brewdogdiy.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for List screen.
 * Featuring a pager to obtain  a paged beer result
 * @param beerRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
class ListScreenViewModel @Inject constructor(
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "ListScreenViewModel"
    }

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> get() = _state

    private val errorMessage = MutableStateFlow<String?>(null)
    private val beerPager = Pager(
        config = PagingConfig(
            pageSize = 25,
            initialLoadSize = 25,
            maxSize = 100
        ),
        remoteMediator = null,
        pagingSourceFactory = { beerRepository.getBeersFromDao() }
    ).flow

    init {
        // Only collect errors here and emit new UI states
        viewModelScope.launch {
            errorMessage.collect {
                _state.value = ViewState(
                    pagingData = beerPager,
                    errorMessage = it
                )
            }
        }
    }

    /**
     * @param beerId A beer ID, 0, or null
     * @return A beer by it's ID or a random beer if the ID is null or equals 0
     */
    fun getBeerOrRandom(beerId: Long?) = when (beerId) {
        0L, null -> getRandomBeer()
        else -> getBeer(beerId)
    }

    /**
     * Get a random beer
     * @return A Flow<Resource<Beer>>
     */
    private fun getRandomBeer() = flow {
        emit(Resource.loading(null))
        beerRepository.getRandomBeerFromDao().collectLatest {
            if (it == null) {
                error("No beers received")
            } else {
                emit(Resource.success(it))
            }
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO)

    /**
     * Get a beer by ID
     * @return A Flow<Resource<Beer>>
     */
    private fun getBeer(beerId: Long) = flow {
        emit(Resource.loading(null))
        beerRepository.getBeerFromDao(beerId).collectLatest {
            emit(Resource.success(it))
        }
    }.catch { exception ->
        emit(Resource.error(exception.message ?: "Unknown error", null))
    }.flowOn(Dispatchers.IO)

}