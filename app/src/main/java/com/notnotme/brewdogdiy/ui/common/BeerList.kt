package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.navigationBarsPadding
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer

@Composable
@ExperimentalPagingApi
fun BeerList(
    modifier: Modifier,
    state: LazyListState,
    pagingItems: LazyPagingItems<Beer>,
    itemClicked: (id: Long) -> Unit,
    itemExtraContent: @Composable (RowScope.(beer: Beer) -> Unit)?,
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { ListItemHeader(pagingItems) }
        items(pagingItems) { item ->
            if (item != null) {
                ListItemBeer(
                    beer = item,
                    onItemClicked = itemClicked,
                    extraContent = itemExtraContent
                )
            } else {
                ListItemPlaceHolder(
                    height = ListItemSize
                )
            }
        }
        item { ListItemFooter(pagingItems) }
        item { Spacer(modifier = Modifier.navigationBarsPadding()) }
    }
}

@Composable
fun ListItemHeader(
    pagingItems: LazyPagingItems<Beer>
) {
    when (val refreshState = pagingItems.loadState.refresh) {
        is LoadState.Loading ->
            ListItemLoading(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.waiting_for_backend)
            )
        is LoadState.Error ->
            ListItemError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = refreshState.error.message!!
            ) {
                pagingItems.retry()
            }
    }
}

@Composable
fun ListItemFooter(
    pagingItems: LazyPagingItems<Beer>
) {
    when (val appendState = pagingItems.loadState.append) {
        is LoadState.Loading ->
            ListItemLoading(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.loading_more_beers)
            )
        is LoadState.Error ->
            ListItemError(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = appendState.error.message!!
            ) {
                pagingItems.retry()
            }
    }
}
