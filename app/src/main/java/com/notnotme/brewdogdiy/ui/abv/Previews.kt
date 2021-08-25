package com.notnotme.brewdogdiy.ui.abv

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.notnotme.brewdogdiy.model.domain.Beer
import kotlinx.coroutines.flow.flow

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun AbvScreenEmptyPreview() {
    AbvScreen(
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = null,
        navigateToBeer = {},
        backAction = {}
    )
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun AbvScreenNotEmptyPreview() {
    AbvScreen(
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = null,
        navigateToBeer = {},
        backAction = {}
    )
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun AbvScreenWithErrorPreview() {
    AbvScreen(
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = "Cannot access the local database",
        navigateToBeer = {},
        backAction = {}
    )
}
