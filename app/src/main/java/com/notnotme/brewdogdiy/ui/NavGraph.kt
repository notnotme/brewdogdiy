package com.notnotme.brewdogdiy.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
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
import com.notnotme.brewdogdiy.ui.beer.BeerScreen
import com.notnotme.brewdogdiy.ui.beer.BeerScreenViewModel
import com.notnotme.brewdogdiy.ui.home.HomeScreen
import com.notnotme.brewdogdiy.ui.list.ListScreen
import com.notnotme.brewdogdiy.ui.list.ListScreenViewModel
import com.notnotme.brewdogdiy.ui.update.UpdateScreen
import com.notnotme.brewdogdiy.ui.update.UpdateScreenViewModel
import com.notnotme.brewdogdiy.util.Resource

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
        ) {
            val arguments = requireNotNull(it.arguments)
            val beerId = arguments.getLong(NavGraphDestinations.BEER_ID_KEY)

            val beerViewModel: BeerScreenViewModel = hiltViewModel(it)
            val beerViewState by beerViewModel.state.collectAsState()

            LaunchedEffect(beerViewModel) {
                beerViewModel.getBeerOrRandom(beerId)
            }

            beerViewState.beerResource?.let { resource ->
                // Update arguments in case of random beer
                if (resource.status == Resource.Companion.Status.Success && beerId == 0L) {
                    arguments.putLong(NavGraphDestinations.BEER_ID_KEY, resource.data!!.id)
                }
            }

            BeerScreen(
                beerResource = beerViewState.beerResource,
                errorMessage = beerViewState.errorMessage,
                backAction = { actions.upPress() }
            )
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
