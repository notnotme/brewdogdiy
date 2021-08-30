package com.notnotme.brewdogdiy.ui.abv

import androidx.paging.PagingData
import com.notnotme.brewdogdiy.model.domain.Beer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * AbvScreen view states
 */
data class ViewState(
    val pagingData: Flow<PagingData<Beer>> = flow { emit(PagingData.empty()) },
    val errorMessage: String? = null
)
