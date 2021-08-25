package com.notnotme.brewdogdiy.ui.ibu

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
 * ViewModel for Ibu screen.
 * Featuring a pager to obtain a paged beer result
 * @param beerRepository An instance of ApiRepository
 */
@HiltViewModel
@ExperimentalPagingApi
class IbuScreenViewModel @Inject constructor(
    private val beerRepository: BeerRepository
) : ViewModel() {

    companion object {
        const val TAG = "IbuScreenViewModel"
    }

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> get() = _state

    private val minIbu = MutableStateFlow(0.0f)
    private val maxIbu = MutableStateFlow(100.0f)
    private val errorMessage = MutableStateFlow<String?>(null)

    init {
        // Combines the latest value from each flow to generate a new ViewState
        viewModelScope.launch {
            combine(
                minIbu,
                maxIbu,
                errorMessage
            ) { minIbu, maxIbu, errorMessage ->
                val beerPager = Pager(
                    config = PagingConfig(
                        pageSize = 25,
                        initialLoadSize = 25,
                        maxSize = 100
                    ),
                    remoteMediator = null,
                    pagingSourceFactory = { beerRepository.getBeersByIbuFromDao(minIbu, maxIbu) }
                ).flow

                ViewState(
                    pagingData = beerPager,
                    minIbu = minIbu,
                    maxIbu = maxIbu,
                    errorMessage = errorMessage
                )
            }.catch { e ->
                Log.e(TAG, "Error: ${e.message}")
            }.collect {
                _state.value = it
            }
        }
    }

    fun setMinIbu(min: Float) {
        minIbu.value = min
    }

    fun setMaxIbu(max: Float) {
        maxIbu.value = max
    }

}
