package com.notnotme.brewdogdiy.ui.ibu

import androidx.paging.PagingData
import com.notnotme.brewdogdiy.model.domain.Beer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * IbuScreen view states
 */
data class ViewState(
    val pagingData: Flow<PagingData<Beer>> = flow { emit(PagingData.empty()) },
    val minIbu: Float = 0.0f,
    val maxIbu: Float = 100.0f,
    val errorMessage: String? = null
)
