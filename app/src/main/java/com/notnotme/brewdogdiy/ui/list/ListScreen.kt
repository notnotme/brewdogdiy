package com.notnotme.brewdogdiy.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
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
import com.notnotme.brewdogdiy.ui.theme.Typography
import com.notnotme.brewdogdiy.util.getYearAsString

@Composable
@ExperimentalPagingApi
fun ListScreen(
    modifier: Modifier = Modifier,
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { ListItemHeader(pagingItems) }
        items(pagingItems) { item ->
            if (item != null) {
                ListItem(item) { navigateToBeer(it) }
            } else {
                ListItemPlaceHolder()
            }
        }
        item { ListItemFooter(pagingItems) }
    }
}

@Composable
fun ListItemHeader(
    pagingItems: LazyPagingItems<Beer>
) {
    when (val refreshState = pagingItems.loadState.refresh) {
        is LoadState.Loading -> ListItemLoading(stringResource(R.string.waiting_for_backend))
        is LoadState.Error -> ListItemError(refreshState.error.message!!) {
            pagingItems.retry()
        }
    }
}

@Composable
fun ListItemFooter(
    pagingItems: LazyPagingItems<Beer>
) {
    when (val appendState = pagingItems.loadState.append) {
        is LoadState.Loading -> ListItemLoading(stringResource(R.string.loading_more_beers))
        is LoadState.Error -> ListItemError(appendState.error.message!!) {
            pagingItems.retry()
        }
    }
}

@Composable
fun ListItem(beer: Beer, onItemClicked: (beerId: Long) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(76.dp)
            .fillMaxWidth()
            .clickable { onItemClicked(beer.id) }) {
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
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1.0f)
                    .fillMaxHeight()
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
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
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

@Composable
fun ListItemError(message: String, onRetryClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(125.dp)
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Text(
            style = Typography.h1,
            text = message,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Button(
            onClick = { onRetryClick() }) {
            Text(text = stringResource(R.string.retry))
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}

@Composable
fun ListItemLoading(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(125.dp)
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Text(
            style = Typography.body1,
            text = message,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        CircularProgressIndicator()
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}

@Composable
fun ListItemPlaceHolder() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(76.dp)
            .fillMaxWidth()) {
    }
}
