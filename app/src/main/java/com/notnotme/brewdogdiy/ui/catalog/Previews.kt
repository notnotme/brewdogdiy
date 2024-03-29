package com.notnotme.brewdogdiy.ui.catalog

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
fun CatalogScreenEmptyPreview() {
    BrewdogTheme {
        CatalogScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = null,
            navigateToBeer = {},
            backAction = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun CatalogScreenNotEmptyPreview() {
    BrewdogTheme {
        CatalogScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = null,
            navigateToBeer = {},
            backAction = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun CatalogScreenWithErrorPreview() {
    BrewdogTheme {
        CatalogScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = "Cannot access the local database",
            navigateToBeer = {},
            backAction = {}
        )
    }
}
