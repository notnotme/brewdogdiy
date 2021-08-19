package com.notnotme.brewdogdiy.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.*
import com.notnotme.brewdogdiy.ui.theme.Typography
import com.notnotme.brewdogdiy.util.getYearAsString

@Composable
@ExperimentalPagingApi
fun ListScreen(
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit,
    backAction: () -> Unit
) {
    if (errorMessage != null) {
        ErrorMessageBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            text = errorMessage,
            space = 16.dp
        )
    } else {
        Scaffold(
            topBar = {
                SimpleAppBar(
                    backAction = backAction
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                color = MaterialTheme.colors.surface
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item { ListItemHeader(pagingItems) }
                    items(pagingItems) { item ->
                        if (item != null) {
                            ListItem(item) { navigateToBeer(it) }
                        } else {
                            ListItemPlaceHolder(
                                height = ListItemSize
                            )
                        }
                    }
                    item { ListItemFooter(pagingItems) }
                }
            }
        }
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
                    .height(ListItemLargeSize)
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.waiting_for_backend)
            )
        is LoadState.Error ->
            ListItemError(
                modifier = Modifier
                    .height(ListItemLargeSize)
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
                    .height(ListItemLargeSize)
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.loading_more_beers)
            )
        is LoadState.Error ->
            ListItemError(
                modifier = Modifier
                    .height(ListItemLargeSize)
                    .fillMaxWidth()
                    .padding(16.dp),
                text = appendState.error.message!!
            ) {
                pagingItems.retry()
            }
    }
}

@Composable
fun ListItem(
    beer: Beer,
    onItemClicked: (beerId: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .height(ListItemSize)
            .fillMaxWidth()
            .clickable { onItemClicked(beer.id) },
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    style = Typography.body1,
                    text = beer.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    style = Typography.caption,
                    text = beer.tagLine,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    style = Typography.overline,
                    text = stringResource(R.string.date_of_birth),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val date = beer.firstBrewed
                val dateString = date?.getYearAsString() ?: stringResource(R.string.no_date_placeholder)

                Text(
                    style = Typography.caption,
                    text = dateString,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
        }
        Divider()
    }
}
