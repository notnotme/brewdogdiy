package com.notnotme.brewdogdiy

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.ExperimentalPagingApi
import com.notnotme.brewdogdiy.ui.MainScreenViewModel
import com.notnotme.brewdogdiy.ui.update.UpdateScreen

@Composable
@ExperimentalPagingApi
fun MainScreen() {
    val viewModel: MainScreenViewModel = hiltViewModel()
    val downloadStatus by viewModel.downloadStatus.collectAsState()

    if (downloadStatus == null || downloadStatus?.isFinished == false) {
        UpdateScreen()
    } else {
        Text(
            text = "OK"
        )
    }
}

