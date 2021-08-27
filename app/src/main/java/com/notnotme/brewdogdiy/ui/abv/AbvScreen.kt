package com.notnotme.brewdogdiy.ui.abv

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.ListScreen
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun AbvScreen(
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit,
    backAction: () -> Unit,
) {
    ListScreen(
        pagingItems = pagingItems,
        errorMessage = errorMessage,
        navigateToBeer = navigateToBeer,
        backAction = backAction,
        backdropContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Filters")
            }
        },
        listItemExtraContent = { beer ->
            Spacer(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
            Text(
                style = Typography.caption,
                text = stringResource(R.string.abv, beer.abv).uppercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        })
}
