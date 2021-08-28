package com.notnotme.brewdogdiy.ui.catalog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.LazyPagingItems
import com.google.accompanist.insets.navigationBarsPadding
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.ErrorMessageBox
import com.notnotme.brewdogdiy.ui.common.TopAppBar

@Composable
@ExperimentalPagingApi
fun CatalogScreen(
    pagingItems: LazyPagingItems<Beer>,
    errorMessage: String? = null,
    navigateToBeer: (id: Long) -> Unit,
    backAction: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backAction = backAction
            )
        }
    ) {
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
                itemClicked = { navigateToBeer(it) },
                itemExtraContent = null
            )
        }
    }
}
