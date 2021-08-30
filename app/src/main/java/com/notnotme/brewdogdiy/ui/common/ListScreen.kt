package com.notnotme.brewdogdiy.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.insets.navigationBarsPadding
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.catalog.BeerList
import kotlinx.coroutines.launch

@Composable
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun ListScreen(
    title: String,
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit,
    backAction: () -> Unit,
    backdropContent: @Composable () -> Unit = {},
    listItemExtraContent: @Composable RowScope.(beer: Beer) -> Unit = {}
) {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutines = rememberCoroutineScope()

    BackdropScaffold(
        scaffoldState = scaffoldState,
        frontLayerShape = MaterialTheme.shapes.medium,
        appBar = {
            TopAppBar(
                title = title,
                backAction = backAction,
                actions = {
                    IconButton(
                        onClick = {
                            coroutines.launch {
                                if (scaffoldState.isConcealed) {
                                    scaffoldState.reveal()
                                } else {
                                    scaffoldState.conceal()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = stringResource(R.string.filter_list)
                        )
                    }
                }
            )
        },
        backLayerBackgroundColor = MaterialTheme.colors.primaryVariant,
        backLayerContent = backdropContent,
        frontLayerContent = {
            if (errorMessage != null) {
                ErrorMessageBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    text = errorMessage,
                    space = 16.dp
                )
            } else {
                val scrollState = rememberLazyListState()
                BeerList(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
                    pagingItems = pagingItems,
                    itemClicked = navigateToBeer,
                    itemExtraContent = listItemExtraContent
                )
            }
        }
    )
}
