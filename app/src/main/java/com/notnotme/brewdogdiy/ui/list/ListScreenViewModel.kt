package com.notnotme.brewdogdiy.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.notnotme.brewdogdiy.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

}
