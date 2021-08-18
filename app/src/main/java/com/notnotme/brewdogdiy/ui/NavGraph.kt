package com.notnotme.brewdogdiy.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import com.notnotme.brewdogdiy.ui.list.ListScreen
import com.notnotme.brewdogdiy.ui.list.ListScreenViewModel

@Composable
@ExperimentalPagingApi
fun NavGraph(
    navHostController: NavHostController,
) {
    val actions = remember(navHostController) { NavGraphActions(navHostController) }
    val listViewModel: ListScreenViewModel = hiltViewModel()
    val listViewState by listViewModel.state.collectAsState()
    val pagingItems = listViewState.pagingData.collectAsLazyPagingItems()

    NavHost(
        navController = navHostController,
        startDestination = NavGraphDestinations.HOME_ROUTE
    ) {
        composable(
            route = NavGraphDestinations.HOME_ROUTE
        ) { navBackStackEntry ->
            ListScreen(
                modifier = Modifier.fillMaxSize(),
                pagingItems = pagingItems,
                errorMessage = listViewState.errorMessage,
                navigateToBeer = { actions.navigateToBeer(it) }
            )
        }
        composable(
            route = "${NavGraphDestinations.BEER_ROUTE}/{${NavGraphDestinations.BEER_ID_KEY}}",
            arguments = listOf(
                navArgument(NavGraphDestinations.BEER_ID_KEY) { type = NavType.LongType }
            )
        ) { navBackStackEntry ->
        }
    }
}

/**
 * Destinations used in the app.
 */
object NavGraphDestinations {
    const val HOME_ROUTE = "home"
    const val BEER_ROUTE = "beer"
    const val BEER_ID_KEY = "beerId"
}

/**
 * Models the navigation actions in the app.
 */
class NavGraphActions(navController: NavHostController) {
    val navigateToBeer: (id: Long) -> Unit = {
        navController.navigate("${NavGraphDestinations.BEER_ROUTE}/$it")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
