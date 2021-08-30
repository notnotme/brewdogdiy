package com.notnotme.brewdogdiy.ui.abv

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
        AbvScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = null,
            navigateToBeer = {},
            backAction = {},
            onSortChanged = {},
            onMinAbvValueChangeFinished = {},
            onMaxAbvValueChangeFinished = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun AbvScreenNotEmptyPreview() {
    BrewdogTheme {
        AbvScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = null,
            navigateToBeer = {},
            backAction = {},
            onSortChanged = {},
            onMinAbvValueChangeFinished = {},
            onMaxAbvValueChangeFinished = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun AbvScreenWithErrorPreview() {
    BrewdogTheme {
        AbvScreen(
            pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
            errorMessage = "Cannot access the local database",
            navigateToBeer = {},
            backAction = {},
            onSortChanged = {},
            onMinAbvValueChangeFinished = {},
            onMaxAbvValueChangeFinished = {}
        )
    }
}
