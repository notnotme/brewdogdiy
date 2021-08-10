package com.notnotme.brewdogdiy.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.notnotme.brewdogdiy.repository.ApiRepository
import com.notnotme.brewdogdiy.repository.BeerPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for List screen.
 * Featuring a pager to obtain  a paged beer result
 * @param apiRepository An instance of ApiRepository
 */
@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val apiRepository: ApiRepository
) : ViewModel() {

    companion object {
        const val TAG = "ListScreenViewModel"
        const val PAGE_SIZE = 25
    }

    val beerPager = Pager(PagingConfig(initialLoadSize = PAGE_SIZE*2, pageSize = PAGE_SIZE), 1) {
        BeerPagingSource(apiRepository)
    }.flow.cachedIn(viewModelScope)

}