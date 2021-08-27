package com.notnotme.brewdogdiy.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.catalog.BeerList
import com.notnotme.brewdogdiy.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
@ExperimentalPagingApi
@ExperimentalMaterialApi
fun ListScreen(
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit,
    backAction: () -> Unit,
    backdropContent: @Composable () -> Unit = {},
    listItemExtraContent: @Composable RowScope.(beer: Beer) -> Unit = {}
) {
    val scrollState = rememberLazyListState()
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val coroutines = rememberCoroutineScope()

    BackdropScaffold(
        scaffoldState = scaffoldState,
        frontLayerShape = MaterialTheme.shapes.medium,
        backLayerBackgroundColor = MaterialTheme.colors.surface,
        appBar = {
            SimpleAppBar(
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
                        }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            tint = contentColorFor(LocalContentColor.current),
                            contentDescription = stringResource(R.string.filter_list)
                        )
                    }
                }
            )
        },
        backLayerContent = backdropContent,
        frontLayerContent = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
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
                    // Force drawer open from right to left by inverting the layout direction
                    // Elevate the AppBar when content scroll
                    val appBarElevation by animateDpAsState(
                        if (scrollState.firstVisibleItemScrollOffset > 1) {
                            AppBarDefaults.TopAppBarElevation
                        } else {
                            0.dp
                        }
                    )

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Surface(
                            elevation = appBarElevation,
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                style = Typography.body1,
                                text = "Results: 1337"
                            )

                        }
                        BeerList(
                            modifier = Modifier.fillMaxSize(),
                            state = scrollState,
                            pagingItems = pagingItems,
                            itemClicked = navigateToBeer,
                            itemExtraContent = listItemExtraContent
                        )
                    }
                }
            }
        }
    )
}
