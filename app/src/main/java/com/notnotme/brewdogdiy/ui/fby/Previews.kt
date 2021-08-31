package com.notnotme.brewdogdiy.ui.fby

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import kotlinx.coroutines.flow.flow

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun AbvScreenEmptyPreview() {
    BrewdogTheme {
        FbyScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = null,
            navigateToBeer = {},
            backAction = {},
            onSortChanged = {},
            onMinYearValueChangeFinished = {},
            onMaxYearValueChangeFinished = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun AbvScreenNotEmptyPreview() {
    BrewdogTheme {
        FbyScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = null,
            navigateToBeer = {},
            backAction = {},
            onSortChanged = {},
            onMinYearValueChangeFinished = {},
            onMaxYearValueChangeFinished = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun AbvScreenWithErrorPreview() {
    BrewdogTheme {
        FbyScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = "Cannot access the local database",
            navigateToBeer = {},
            backAction = {},
            onSortChanged = {},
            onMinYearValueChangeFinished = {},
            onMaxYearValueChangeFinished = {}
        )
    }
}
