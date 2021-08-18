package com.notnotme.brewdogdiy.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import com.notnotme.brewdogdiy.ui.home.HomeScreen
import com.notnotme.brewdogdiy.ui.list.ListScreen
import com.notnotme.brewdogdiy.ui.list.ListScreenViewModel
import com.notnotme.brewdogdiy.ui.update.UpdateScreen
import com.notnotme.brewdogdiy.ui.update.UpdateScreenViewModel

@Composable
@ExperimentalPagingApi
fun NavGraph(
    navHostController: NavHostController,
) {
    val actions = remember(navHostController) { NavGraphActions(navHostController) }

    NavHost(
        navController = navHostController,
        startDestination = NavGraphDestinations.HOME_ROUTE
    ) {
        composable(
            route = NavGraphDestinations.HOME_ROUTE
        ) {
            HomeScreen(
                buttonListClicked = { actions.navigateToList() },
                buttonRandomClicked = { actions.navigateToBeer(0L) },
                onUpdateClick = { actions.navigateToUpdate() }
            )
        }
        composable(
            route = NavGraphDestinations.LIST_ROUTE
        ) {
            val listViewModel: ListScreenViewModel = hiltViewModel(it)
            val listViewState by listViewModel.state.collectAsState()
            val pagingItems = listViewState.pagingData.collectAsLazyPagingItems()
            ListScreen(
                pagingItems = pagingItems,
                errorMessage = listViewState.errorMessage,
                navigateToBeer = { actions.navigateToBeer(it) },
                backAction = { actions.upPress() }
            )
        }
        composable(
            route = "${NavGraphDestinations.BEER_ROUTE}/{${NavGraphDestinations.BEER_ID_KEY}}",
            arguments = listOf(
                navArgument(NavGraphDestinations.BEER_ID_KEY) { type = NavType.LongType }
            )
        ) { navBackStackEntry ->
            // todo
        }
        composable(
            route = NavGraphDestinations.UPDATE_ROUTE
        ) {
            val updateViewModel: UpdateScreenViewModel = hiltViewModel(it)
            val updateViewState by updateViewModel.state.collectAsState()
            UpdateScreen(
                updating = updateViewState.updating,
                errorMessage = updateViewState.errorMessage,
                downloadStatus = updateViewState.downloadStatus,
                onUpdateButtonClick = { updateViewModel.queueUpdate() },
                backAction = { actions.upPress() }
            )
        }
    }
}

/**
 * Destinations used in the app.
 */
object NavGraphDestinations {
    const val HOME_ROUTE = "home"
    const val UPDATE_ROUTE = "update"
    const val LIST_ROUTE = "list"
    const val BEER_ROUTE = "beer"
    const val BEER_ID_KEY = "beerId"
}

/**
 * Models the navigation actions in the app.
 */
class NavGraphActions(navController: NavHostController) {
    val navigateToUpdate: () -> Unit = {
        navController.navigate(NavGraphDestinations.UPDATE_ROUTE)
    }
    val navigateToList: () -> Unit = {
        navController.navigate(NavGraphDestinations.LIST_ROUTE)
    }
    val navigateToBeer: (id: Long) -> Unit = {
        navController.navigate("${NavGraphDestinations.BEER_ROUTE}/$it")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
