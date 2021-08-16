package com.notnotme.brewdogdiy

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.ExperimentalPagingApi
import com.notnotme.brewdogdiy.ui.MainScreenViewModel
import com.notnotme.brewdogdiy.ui.update.UpdateScreen

@Composable
@ExperimentalPagingApi
fun MainScreen() {
    val viewModel: MainScreenViewModel = hiltViewModel()
    val downloadStatus by viewModel.downloadStatus.collectAsState()
    var iWasHere by rememberSaveable { mutableStateOf(false) }

    if (!iWasHere && downloadStatus == null) {
        /*
         * If we never goes trough this path and download status is null
         * we will show the update screen to the user
         */
        UpdateScreen()
    } else {
        /*
         * iWasHere allow us to later delete the app data without being teleported here
         * by collecting downloadStatus again (because it was deleted).
         * The UpdateScreen is showed in another place in the application and allow the
         * user to force update the app data.
         */
        iWasHere = true
    }

}

