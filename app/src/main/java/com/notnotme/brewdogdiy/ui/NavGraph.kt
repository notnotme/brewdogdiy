package com.notnotme.brewdogdiy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.notnotme.brewdogdiy.ui.beer.BeerScreen
import com.notnotme.brewdogdiy.ui.list.ListScreen
import com.notnotme.brewdogdiy.ui.start.StartScreen
import com.notnotme.brewdogdiy.util.Resource
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
@ExperimentalPagingApi
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.START_ROUTE
) {
    val viewModel: MainActivityViewModel = hiltViewModel()
    val actions = remember(navController) { MainActions(navController) }
    val pagingItems = remember { viewModel.beerPager }.collectAsLazyPagingItems()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = MainDestinations.START_ROUTE) {
            StartScreen(
                buttonListClicked = { actions.navigateToList(it) },
                buttonRandomClicked = { actions.navigateToRandomBeer(it) }
            )
        }
        composable(route = MainDestinations.LIST_ROUTE) {
            ListScreen(
                pagingItems = pagingItems,
                onListItemClicked = { beerId -> actions.navigateToBeer(beerId, it) }
            )
        }
        composable(
            route = "${MainDestinations.BEER_ROUTE}?${MainDestinations.BEER_ID_KEY}={${MainDestinations.BEER_ID_KEY}}",
            arguments = listOf(
                navArgument(MainDestinations.BEER_ID_KEY) {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            )
        ) { navBackStackEntry ->
            val arguments = requireNotNull(navBackStackEntry.arguments)
            val beerId = arguments.getLong(MainDestinations.BEER_ID_KEY)
            val beer by remember { viewModel.getBeerOrRandom(beerId) }.collectAsState(null)

            beer?.let {
                // Update arguments in case of random beer
                if (it.status == Resource.Companion.Status.Success && beerId == 0L) {
                    arguments.putLong(MainDestinations.BEER_ID_KEY, it.data!!.id)
                }
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
    val navigateToList = { from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.LIST_ROUTE)
        }
    }

    // Used from START_ROUTE
    val navigateToRandomBeer = { from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.BEER_ROUTE)
        }
    }

    // Used from LIST_ROUTE
    val navigateToBeer = { beerId: Long, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate("${MainDestinations.BEER_ROUTE}?${MainDestinations.BEER_ID_KEY}=$beerId")
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
