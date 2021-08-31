package com.notnotme.brewdogdiy.ui.fby

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
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for Abv screen.
 * Featuring a pager to obtain a paged beer result
 * @param beerRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
class FbyScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "FbyScreenViewModel"
        const val STATE_MIN_YEAR = "minYear"
        const val STATE_MAX_YEAR = "maxYear"
        const val STATE_ORDER_BY_DESC = "order"
    }

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> get() = _state

    private val orderByDesc = MutableStateFlow(savedStateHandle[STATE_ORDER_BY_DESC] ?: false)
    private val minYear = MutableStateFlow(savedStateHandle[STATE_MIN_YEAR] ?: 2000)
    private val maxYear = MutableStateFlow(savedStateHandle[STATE_MAX_YEAR] ?: Calendar.getInstance().get(Calendar.YEAR))
    private val errorMessage = MutableStateFlow<String?>(null)

    init {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 0L

        // Combines the latest value from each flow to generate a new ViewState
        viewModelScope.launch {
            combine(
                orderByDesc,
                minYear,
                maxYear,
                errorMessage
            ) { orderByDesc, minYear, maxYear, errorMessage ->
                // Save in bundle
                savedStateHandle[STATE_ORDER_BY_DESC] = orderByDesc
                savedStateHandle[STATE_MIN_YEAR] = minYear
                savedStateHandle[STATE_MAX_YEAR] = maxYear

                calendar.set(Calendar.YEAR, minYear)
                calendar.set(Calendar.MONTH, Calendar.JANUARY)
                val minDate = Date(calendar.timeInMillis)

                calendar.set(Calendar.YEAR, maxYear)
                calendar.set(Calendar.MONTH, Calendar.DECEMBER)
                val maxDate = Date(calendar.timeInMillis)

                // Send to view
                val beerPager = Pager(
                    config = PagingConfig(
                        pageSize = 25,
                        initialLoadSize = 75,
                        maxSize = 100
                    ),
                    remoteMediator = null,
                    pagingSourceFactory = { beerRepository.getBeersByBrewDateFromDao(minDate, maxDate, orderByDesc) }
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

    fun setMinYear(min: Int) {
        minYear.value = min
    }

    fun setMaxYear(max: Int) {
        maxYear.value = max
    }

    fun setOrderByDesc(value: Boolean) {
        orderByDesc.value = value
    }

}
