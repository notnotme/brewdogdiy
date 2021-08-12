package com.notnotme.brewdogdiy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.notnotme.brewdogdiy.ui.beer.BeerScreen
import com.notnotme.brewdogdiy.ui.list.ListScreen
import com.notnotme.brewdogdiy.ui.start.StartScreen
import com.notnotme.brewdogdiy.util.Resource
import com.notnotme.brewdogdiy.viewmodel.MainActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Destinations used in the application.
 */
object MainDestinations {
    const val START_ROUTE = "start"
    const val LIST_ROUTE = "list"
    const val BEER_ROUTE = "beer"
    const val BEER_ID_KEY = "beerId"
}

@Composable
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.START_ROUTE
) {
    val viewModel: MainActivityViewModel = hiltViewModel()
    val actions = remember(navController) { MainActions(navController) }
    val pagingItems = remember { viewModel.beerPager }.collectAsLazyPagingItems()
    val beer = rememberSaveable(viewModel.beer.value) { viewModel.beer }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = MainDestinations.START_ROUTE) {
            StartScreen(
                buttonListClicked = { actions.openList(it) },
                buttonRandomClicked = {
                    viewModel.getBeerOrRandom(null)
                    actions.openRandomBeer(it)
                }
            )
        }
        composable(route = MainDestinations.LIST_ROUTE) {
            ListScreen(
                pagingItems = pagingItems,
                onListItemClicked = { beerId ->
                    run {
                        viewModel.getBeerOrRandom(beerId)
                        actions.openBeer(beerId, it)
                    }
                }
            )
        }
        composable(
            route = "${MainDestinations.BEER_ROUTE}/{${MainDestinations.BEER_ID_KEY}}",
            arguments = listOf(
                navArgument(MainDestinations.BEER_ID_KEY) {  type = NavType.LongType }
            )
        ) { navBackStackEntry ->
            if (beer.value?.status == Resource.Companion.Status.Success) {
                val arguments = requireNotNull(navBackStackEntry.arguments)
                val beerId = arguments.getLong(MainDestinations.BEER_ID_KEY)
                arguments.putLong(MainDestinations.BEER_ID_KEY, beerId)
            }
            BeerScreen(
                beer = beer
            )
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
@ExperimentalCoroutinesApi
class MainActions(navController: NavHostController) {
    // Used from START_ROUTE
    val openList = { from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.LIST_ROUTE)
        }
    }

    // Used from START_ROUTE
    val openRandomBeer = { from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.BEER_ROUTE}/0")
        }
    }

    // Used from LIST_ROUTE
    val openBeer = { beerId: Long, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.BEER_ROUTE}/$beerId")
        }
    }

}


/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
