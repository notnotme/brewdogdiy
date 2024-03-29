package com.notnotme.brewdogdiy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.notnotme.brewdogdiy.ui.update.UpdateScreen
import com.notnotme.brewdogdiy.ui.update.UpdateScreenViewModel

@Composable
@ExperimentalMaterialApi
@ExperimentalPagingApi
@ExperimentalCoilApi
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fill status bar background with primary color
        val statusBarInsets =
            rememberInsetsPaddingValues(LocalWindowInsets.current.statusBars.layoutInsets)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarInsets.calculateTopPadding())
                .background(MaterialTheme.colors.primary),
        )

        // Show the Update screen if the app found no data, otherwise let's show the NavGraph
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
            LaunchedEffect(viewModel) {
                iWasHere = true
            }

            val navHostController = rememberNavController()
            NavGraph(
                navHostController = navHostController
            )
        }
    }
}

