package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.theme.Typography
import java.util.*

val ListItemSize = 76.dp
val ListItemLargeSize = 125.dp

@Composable
fun ListItemBeer(
    beer: Beer,
    onItemClicked: (beerId: Long) -> Unit,
    extraContent: @Composable (RowScope.(beer: Beer) -> Unit)?,
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
                .weight(1.0f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
            Column(
                modifier = Modifier
                    .weight(1.0f)
            ) {
                Text(
                    style = Typography.body1,
                    text = beer.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        style = Typography.caption,
                        text = beer.tagLine,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            extraContent?.invoke(this, beer)
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
fun ListItemError(
    modifier: Modifier = Modifier,
    text: String,
    onRetryClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            style = Typography.body1,
            text = text,
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
    }
}

@Composable
fun ListItemLoading(
    modifier: Modifier = Modifier,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            style = Typography.body1,
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        CircularProgressIndicator()
    }
}

@Composable
fun ListItemPlaceHolder(
    height: Dp
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(height)
            .fillMaxWidth()) {
    }
}

// region Previews

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListItemBeerPreview() {
    ListItemBeer(
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
        ),
        onItemClicked =  {},
        extraContent = null
    )
}

@Composable
@Preview(showBackground = true)
@ExperimentalPagingApi
fun ListItemBeerExtraContentPreview() {
    ListItemBeer(
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
        ),
        onItemClicked =  {}
    ) { beer ->
        Spacer(
            modifier = Modifier
                .width(8.dp)
                .fillMaxHeight()
        )
        Text(
            style = Typography.caption,
            text = "#${beer.id}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ListItemLoadingPreview() {
    ListItemLoading(
        modifier = Modifier.height(ListItemLargeSize).fillMaxWidth().padding(16.dp),
        text = "Loading..."
    )
}

@Composable
@Preview(showBackground = true)
fun ListItemErrorPreview() {
    ListItemError(
        modifier = Modifier.height(ListItemLargeSize).fillMaxWidth().padding(16.dp),
        text = "Unknown error"
    ) {}
}

@Composable
@Preview(showBackground = true)
fun ListItemPlaceHolderPreview() {
    ListItemPlaceHolder(
        height = ListItemLargeSize
    )
}

// endregion Previews

