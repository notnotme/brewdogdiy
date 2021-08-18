package com.notnotme.brewdogdiy.ui.beer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.ErrorMessageBox
import com.notnotme.brewdogdiy.ui.common.LoadingMessageBox
import com.notnotme.brewdogdiy.ui.common.SimpleAppBar
import com.notnotme.brewdogdiy.util.Resource

@Composable
fun BeerScreen(
    beerResource: Resource<Beer>? = null,
    errorMessage: String? = null,
    backAction: () -> Unit
) {
    Scaffold(
        topBar = {
            SimpleAppBar(
                backAction = backAction
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    beerResource?.let {
                        when (beerResource.status) {
                            // todo
                            Resource.Companion.Status.Success -> {
                                Text(text = "beer: ${beerResource.data!!.id}")
                            }
                            Resource.Companion.Status.Error -> {
                                ErrorMessageBox(
                                    text = errorMessage ?: "Unknown error",
                                    space = 16.dp
                                )
                            }
                            Resource.Companion.Status.Loading -> {
                                LoadingMessageBox(
                                    text = stringResource(R.string.loading),
                                    space = 16.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
