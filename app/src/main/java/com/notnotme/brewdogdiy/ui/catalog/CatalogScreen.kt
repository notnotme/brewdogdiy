package com.notnotme.brewdogdiy.ui.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.ErrorMessageBox
import com.notnotme.brewdogdiy.ui.common.SimpleAppBar

@Composable
@ExperimentalPagingApi
fun CatalogScreen (
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit,
    backAction: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    Scaffold(
        topBar = {
            SimpleAppBar(
                backAction = backAction
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
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
                BeerList(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
                    pagingItems = pagingItems,
                    itemClicked = { navigateToBeer(it) },
                    itemExtraContent = null
                )
            }
        }
    }
}
