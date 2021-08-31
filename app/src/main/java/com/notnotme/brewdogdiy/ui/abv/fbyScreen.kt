package com.notnotme.brewdogdiy.ui.abv

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.CheckableTextButton
import com.notnotme.brewdogdiy.ui.common.ListScreen
import com.notnotme.brewdogdiy.ui.common.Slider
import com.notnotme.brewdogdiy.ui.theme.Typography
import okhttp3.internal.format

@Composable
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun AbvScreen(
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit,
    backAction: () -> Unit,
    onSortChanged: (orderByDesc: Boolean) -> Unit,
    onMaxAbvValueChangeFinished: (value: Float) -> Unit,
    onMinAbvValueChangeFinished: (value: Float) -> Unit,
) {
    var orderByDesc by rememberSaveable { mutableStateOf(false) }
    var minAbv by rememberSaveable { mutableStateOf(0.0f) }
    var maxAbv by rememberSaveable { mutableStateOf(100.0f) }

    ListScreen(
        title = stringResource(R.string.screen_abv_title),
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
                    text = stringResource(R.string.sort)
                )
                Spacer(
                    modifier = Modifier.padding(4.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CheckableTextButton(
                        modifier = Modifier.weight(1.0f),
                        text = stringResource(R.string.ascending),
                        checked = !orderByDesc,
                        uncheckedBackgroundColor = MaterialTheme.colors.primaryVariant,
                        onClick = {
                            orderByDesc = false
                            onSortChanged(false)
                        }
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    CheckableTextButton(
                        modifier = Modifier.weight(1.0f),
                        text = stringResource(R.string.descending),
                        checked = orderByDesc,
                        uncheckedBackgroundColor = MaterialTheme.colors.primaryVariant,
                        onClick = {
                            orderByDesc = true
                            onSortChanged(true)
                        }
                    )
                }
                Spacer(
                    modifier = Modifier.padding(8.dp)
                )
                Row {
                    Text(
                        modifier = Modifier.weight(1.0f),
                        text = stringResource(R.string.min_abv)
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = format("%.1f%%", minAbv)
                    )
                }
                Slider(
                    value = minAbv,
                    steps = 0,
                    valueRange = 0.0f..100.0f,
                    onValueChange = { minAbv = it },
                    onValueChangeFinished = { onMinAbvValueChangeFinished(minAbv) }
                )
                Spacer(
                    modifier = Modifier.padding(8.dp)
                )
                Row {
                    Text(
                        modifier = Modifier.weight(1.0f),
                        text = stringResource(R.string.max_abv)
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = format("%.1f%%", maxAbv)
                    )
                }
                Slider(
                    value = maxAbv,
                    steps = 0,
                    valueRange = 0.0f..100.0f,
                    onValueChange = { maxAbv = it },
                    onValueChangeFinished = { onMaxAbvValueChangeFinished(maxAbv) }
                )
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
