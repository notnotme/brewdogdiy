package com.notnotme.brewdogdiy.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.notnotme.brewdogdiy.model.domain.Beer
import kotlinx.coroutines.flow.flow
import java.util.*

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListItem() {
    ListItem(
        beer = Beer(
            id = 1L,
            name = "A random beer",
            tagLine = "Yet another random beer",
            contributedBy = "romain",
            firstBrewed = Date(),
            description = "bla bla...",
            abv = 5.0f,
            ibu = 0.5f,
            imageUrl = null
        )
    ) {}
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListScreenEmpty() {
    ListScreen(
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = null,
        navigateToBeer = {},
        backAction = {}
    )
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListScreenNotEmpty() {
    ListScreen(
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = null,
        navigateToBeer = {},
        backAction = {}
    )
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListScreenWithError() {
    ListScreen(
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = "Cannot access the local database",
        navigateToBeer = {},
        backAction = {}
    )
}
