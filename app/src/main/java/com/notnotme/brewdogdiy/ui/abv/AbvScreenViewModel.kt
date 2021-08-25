package com.notnotme.brewdogdiy.ui.abv

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.notnotme.brewdogdiy.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Abv screen.
 * Featuring a pager to obtain a paged beer result
 * @param beerRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
class AbvScreenViewModel @Inject constructor(
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "AbvScreenViewModel"
    }

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> get() = _state

    private val minAbv = MutableStateFlow(0.0f)
    private val maxAbv = MutableStateFlow(100.0f)
    private val errorMessage = MutableStateFlow<String?>(null)

    init {
        // Combines the latest value from each flow to generate a new ViewState
        viewModelScope.launch {
            combine(
                minAbv,
                maxAbv,
                errorMessage
            ) { minAbv, maxAbv, errorMessage ->
                val beerPager = Pager(
                    config = PagingConfig(
                        pageSize = 25,
                        initialLoadSize = 25,
                        maxSize = 100
                    ),
                    remoteMediator = null,
                    pagingSourceFactory = { beerRepository.getBeersByAbvFromDao(minAbv, maxAbv) }
                ).flow

                ViewState(
                    pagingData = beerPager,
                    minAbv = minAbv,
                    maxAbv = maxAbv,
                    errorMessage = errorMessage
                )
            }.catch { e ->
                Log.e(TAG, "Error: ${e.message}")
            }.collect {
                _state.value = it
            }
        }
    }

    fun setMinAbv(min: Float) {
        minAbv.value = min
    }

    fun setMaxAbv(max: Float) {
        maxAbv.value = max
    }

}
