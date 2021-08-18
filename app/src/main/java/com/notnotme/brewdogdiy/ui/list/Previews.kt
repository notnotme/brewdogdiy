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

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListScreenEmpty() {
    ListScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = null,
        navigateToBeer = {}
    )
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListScreenNotEmpty() {
    ListScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = null,
        navigateToBeer = {}
    )
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListScreenWithError() {
    ListScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        pagingItems = flow { emit(PagingData.empty<Beer>()) }.collectAsLazyPagingItems(),
        errorMessage = "Cannot access the local database",
        navigateToBeer = {}
    )
}
