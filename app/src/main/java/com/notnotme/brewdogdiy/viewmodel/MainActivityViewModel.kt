package com.notnotme.brewdogdiy.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for List screen.
 * Featuring a pager to obtain  a paged beer result
 * @param apiRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
) : ViewModel() {

    companion object {
        const val TAG = "MainActivityViewModel"
        const val PAGE_SIZE = 25
    }

    val beerPager = Pager(PagingConfig(initialLoadSize = PAGE_SIZE * 2, pageSize = PAGE_SIZE)) {
        BeerPagingSource(apiRepository)
    }.flow.cachedIn(viewModelScope)

    private val _beer: MutableState<Resource<Beer>?> = mutableStateOf(null)
    val beer: State<Resource<Beer>?> get() = _beer

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
    private fun getRandomBeer() = viewModelScope.launch(Dispatchers.IO) {
        _beer.value = Resource.loading(null)
        apiRepository.getRandomBeer()
            .catch { exception ->
                _beer.value = Resource.error(exception.message?:"Unknown error", null)
            }.collectLatest {
                val body = it.body()
                if (!it.isSuccessful || body == null) {
                    _beer.value = Resource.error(it.message(), null)
                } else {
                    _beer.value = Resource.success(body[0])
                }
            }
    }

    /**
     * Get a beer from the backend API by Id
     * @return A producer of Resource<Beer>
     */
    private fun getBeer(beerId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _beer.value = Resource.loading(null)
        apiRepository.getBeer(beerId)
            .catch { exception ->
                _beer.value = Resource.error(exception.message?:"Unknown error", null)
            }.collectLatest {
            val body = it.body()
            if (!it.isSuccessful || body == null) {
                _beer.value = Resource.error(it.message(), null)
            } else {
                _beer.value = Resource.success(body[0])
            }
        }
    }

}