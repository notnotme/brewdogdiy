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
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.*
import com.notnotme.brewdogdiy.repository.ApiDataSource
import com.notnotme.brewdogdiy.repository.ApiRepository
import com.notnotme.brewdogdiy.repository.ApiService
import com.notnotme.brewdogdiy.repository.BeerPagingSource
import com.notnotme.brewdogdiy.ui.theme.BrewdogDIYTheme
import com.notnotme.brewdogdiy.ui.theme.Typography
import retrofit2.Response

@Composable
fun ListScreen(
    pagingItems: LazyPagingItems<Beer>,
    onItemClicked: (beerId: Long) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally) {
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
            ListItem(item!!, onItemClicked)
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
    Box (
        modifier = Modifier
            .height(76.dp)
            .clickable { onItemClicked(beer.id) }) {

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)) {

            Text(
                text = beer.name ?: stringResource(R.string.no_name_provided),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Typography.body1)

            Text(
                text = beer.tagLine ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Typography.caption)
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
            .fillMaxWidth()
            .padding(0.dp, 16.dp)) {

        Text(
            text = stringResource(R.string.error_s, message),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis)

        Button(
            onClick = { onRetryClick() },
            modifier = Modifier
                .padding(8.dp)) {
                    Text(text = stringResource(R.string.retry))
                }
    }
}

@Composable
fun ListItemLoading(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 16.dp)) {

        Text(
            text = message,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)

        CircularProgressIndicator(
            modifier = Modifier
                .padding(8.dp))
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
            volume = Value(0.0f, "liter"))

        ListItem(beer, {})
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

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    BrewdogDIYTheme {
        val pagingItems = Pager(PagingConfig(initialLoadSize = ListScreenViewModel.PAGE_SIZE *2, pageSize = ListScreenViewModel.PAGE_SIZE), 1) {
            BeerPagingSource(ApiRepository(ApiDataSource(object: ApiService {
                override suspend fun getBeer(id: Long) = Response.success(BeerList())
                override suspend fun getRandomBeer() = Response.success(BeerList())
                override suspend fun getBeers(page: Int, perPage: Int) = Response.success(BeerList())
            })))
        }.flow.collectAsLazyPagingItems()

        ListScreen(pagingItems) {}
    }
}

// endregion Previews
