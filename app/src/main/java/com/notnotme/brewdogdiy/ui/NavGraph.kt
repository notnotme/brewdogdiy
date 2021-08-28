package com.notnotme.brewdogdiy.ui

import androidx.compose.foundation.background
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.notnotme.brewdogdiy.ui.abv.AbvScreen
import com.notnotme.brewdogdiy.ui.abv.AbvScreenViewModel
import com.notnotme.brewdogdiy.ui.beer.BeerScreen
import com.notnotme.brewdogdiy.ui.beer.BeerScreenViewModel
import com.notnotme.brewdogdiy.ui.catalog.CatalogScreen
import com.notnotme.brewdogdiy.ui.catalog.CatalogScreenViewModel
import com.notnotme.brewdogdiy.ui.home.HomeScreen
import com.notnotme.brewdogdiy.ui.ibu.IbuScreen
import com.notnotme.brewdogdiy.ui.ibu.IbuScreenViewModel
import com.notnotme.brewdogdiy.ui.update.UpdateScreen
import com.notnotme.brewdogdiy.ui.update.UpdateScreenViewModel
import com.notnotme.brewdogdiy.util.Resource

@Composable
@ExperimentalCoilApi
@ExperimentalPagingApi
@ExperimentalMaterialApi
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
                navigateToCatalog = { actions.navigateToCatalog() },
                navigateToRandom = { actions.navigateToBeer(0L) },
                navigateToAbv = { actions.navigateToAbv() },
                navigateToIbu = { actions.navigateToIbu() },
                onUpdateClick = { actions.navigateToUpdate() }
            )
        }
        composable(
            route = NavGraphDestinations.CATALOG_ROUTE
        ) {
            val catalogViewModel: CatalogScreenViewModel = hiltViewModel(it)
            val catalogViewState by catalogViewModel.state.collectAsState()
            val pagingItems = catalogViewState.pagingData.collectAsLazyPagingItems()
            CatalogScreen(
                pagingItems = pagingItems,
                errorMessage = catalogViewState.errorMessage,
                navigateToBeer = { id -> actions.navigateToBeer(id) },
                backAction = { actions.upPress() }
            )
        }
        composable(
            route = NavGraphDestinations.ABV_ROUTE
        ) {
            val abvViewModel: AbvScreenViewModel = hiltViewModel(it)
            val abvViewState by abvViewModel.state.collectAsState()
            val pagingItems = abvViewState.pagingData.collectAsLazyPagingItems()
            AbvScreen(
                pagingItems = pagingItems,
                errorMessage = abvViewState.errorMessage,
                navigateToBeer = { id -> actions.navigateToBeer(id) },
                backAction = { actions.upPress() }
            )
        }
        composable(
            route = NavGraphDestinations.IBU_ROUTE
        ) {
            val ibuViewModel: IbuScreenViewModel = hiltViewModel(it)
            val ibuViewState by ibuViewModel.state.collectAsState()
            val pagingItems = ibuViewState.pagingData.collectAsLazyPagingItems()
            IbuScreen(
                pagingItems = pagingItems,
                errorMessage = ibuViewState.errorMessage,
                navigateToBeer = { id -> actions.navigateToBeer(id) },
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
    const val CATALOG_ROUTE = "catalog"
    const val ABV_ROUTE = "abv"
    const val IBU_ROUTE = "ibu"
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
    val navigateToCatalog: () -> Unit = {
        navController.navigate(NavGraphDestinations.CATALOG_ROUTE)
    }
    val navigateToAbv: () -> Unit = {
        navController.navigate(NavGraphDestinations.ABV_ROUTE)
    }
    val navigateToIbu: () -> Unit = {
        navController.navigate(NavGraphDestinations.IBU_ROUTE)
    }
    val navigateToBeer: (id: Long) -> Unit = {
        navController.navigate("${NavGraphDestinations.BEER_ROUTE}/$it")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
