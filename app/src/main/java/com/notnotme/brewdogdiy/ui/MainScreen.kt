package com.notnotme.brewdogdiy.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import com.notnotme.brewdogdiy.ui.update.UpdateScreen
import com.notnotme.brewdogdiy.ui.update.UpdateScreenViewModel

@Composable
@ExperimentalPagingApi
fun MainScreen() {
    val viewModel: MainScreenViewModel = hiltViewModel()
    val downloadStatus by viewModel.downloadStatus.collectAsState()
    var iWasHere by rememberSaveable { mutableStateOf(false) }

    if (!iWasHere && downloadStatus == null) {
        /*
         * If we never goes trough this path and download status is null
         * we will show the update screen to the user before going into the
         * other path because we need to build the database at least once
         * to be able to use the app.
         */
        val updateViewModel: UpdateScreenViewModel = hiltViewModel()
        val updateViewState by updateViewModel.state.collectAsState()
        UpdateScreen(
            updating = updateViewState.updating,
            errorMessage = updateViewState.errorMessage,
            downloadStatus = updateViewState.downloadStatus,
            onUpdateButtonClick = { updateViewModel.queueUpdate() }
        )
    } else {
        /*
         * iWasHere allow us to later delete the app data without being teleported here
         * by collecting downloadStatus again (because it was deleted).
         * The UpdateScreen is showed in another place in the application and allow the
         * user to force rebuild the database.
         */
         SideEffect {
             iWasHere = true
         }

        val navHostController = rememberNavController()
        NavGraph(
            navHostController = navHostController
        )
    }

}

