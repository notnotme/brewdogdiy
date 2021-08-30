package com.notnotme.brewdogdiy.ui.abv

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
    private val savedStateHandle: SavedStateHandle,
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "AbvScreenViewModel"
        const val STATE_MIN_ABV = "minAbv"
        const val STATE_MAX_ABV = "maxABv"
        const val STATE_ORDER_BY_DESC = "order"
    }

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> get() = _state

    private val orderByDesc = MutableStateFlow(savedStateHandle[STATE_ORDER_BY_DESC] ?: false)
    private val minAbv = MutableStateFlow(savedStateHandle[STATE_MIN_ABV] ?: 0.0f)
    private val maxAbv = MutableStateFlow(savedStateHandle[STATE_MAX_ABV] ?: 100.0f)
    private val errorMessage = MutableStateFlow<String?>(null)

    init {
        // Combines the latest value from each flow to generate a new ViewState
        viewModelScope.launch {
            combine(
                orderByDesc,
                minAbv,
                maxAbv,
                errorMessage
            ) { orderByDesc, minAbv, maxAbv, errorMessage ->
                // Save in bundle
                savedStateHandle[STATE_ORDER_BY_DESC] = orderByDesc
                savedStateHandle[STATE_MIN_ABV] = minAbv
                savedStateHandle[STATE_MAX_ABV] = maxAbv

                // Send to view
                val beerPager = Pager(
                    config = PagingConfig(
                        pageSize = 25,
                        initialLoadSize = 75,
                        maxSize = 100
                    ),
                    remoteMediator = null,
                    pagingSourceFactory = { beerRepository.getBeersByAbvFromDao(minAbv, maxAbv, orderByDesc) }
                ).flow

                ViewState(
                    pagingData = beerPager,
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

    fun setOrderByDesc(value: Boolean) {
        orderByDesc.value = value
    }

}
