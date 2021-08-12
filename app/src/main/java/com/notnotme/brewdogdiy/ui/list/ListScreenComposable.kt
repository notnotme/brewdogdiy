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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.*
import com.notnotme.brewdogdiy.ui.theme.BrewdogDIYTheme
import com.notnotme.brewdogdiy.ui.theme.Typography
import com.notnotme.brewdogdiy.util.StringKt.toDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@Composable
@ExperimentalCoroutinesApi
fun ListScreen(
    pagingItems: LazyPagingItems<Beer>,
    onListItemClicked: (Long) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Check refresh state and show status to user
        item {
            when (val refreshState = pagingItems.loadState.refresh) {
                is LoadState.Loading -> ListItemLoading(stringResource(R.string.waiting_for_backend))
                is LoadState.Error -> ListItemError(refreshState.error.message!!) {
                    pagingItems.retry()
                }
            }
        }

        items(pagingItems) { item ->
            ListItem(item!!, onListItemClicked)
        }

        // Check loadState state and show status to user
        item {
            when (val appendState = pagingItems.loadState.append) {
                is LoadState.Loading -> ListItemLoading(stringResource(R.string.loading_more_beers))
                is LoadState.Error -> ListItemError(appendState.error.message!!) {
                    pagingItems.retry()
                }
            }
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
                    text = beer.name ?: stringResource(R.string.no_name_provided),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    style = Typography.caption,
                    text = beer.tagLine ?: "",
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

                val date = beer.firstBrewed?.toDate()
                val dateString = if (date != null) {
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    calendar.get(Calendar.YEAR).toString()
                } else stringResource(R.string.no_date_placeholder)

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
            style = Typography.body1,
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

// region Previews

@Composable
@Preview(showBackground = true)
fun ListItemPreview() {
    BrewdogDIYTheme {
        val beer = Beer(
            id = 1337L,
            name = "Super Beer",
            tagLine = "The beer to drink.",
            firstBrewed = "2021",
            description = "A beer that is called THE beer",
            abv = 0.0f,
            attenuationLevel = 0.0f,
            boilVolume = Value(0.0f, "celcius"),
            brewersTips = "",
            contributedBy = "",
            ebc = 0.0f,
            foodPairing = listOf(),
            ibu = 0.0f,
            imageUrl = "",
            ingredients = Ingredients(listOf(), listOf(), ""),
            method = Method(listOf(), Temp(Value(0.0f, "seconds"), 0), twist = ""),
            ph = 0.0f,
            srm = 0.0f,
            targetFg = 0.0f,
            targetOg = 0.0f,
            volume = Value(0.0f, "liter")
        )

        ListItem(beer) {}
    }
}

@Composable
@Preview(showBackground = true)
fun ListItemErrorPreview() {
    BrewdogDIYTheme {
        ListItemError("Timeout") {}
    }
}

@Composable
@Preview(showBackground = true)
fun ListItemLoadingPreview() {
    BrewdogDIYTheme {
        ListItemLoading(stringResource(R.string.loading_more_beers))
    }
}

// endregion Previews
