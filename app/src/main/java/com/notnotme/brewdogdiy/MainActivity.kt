package com.notnotme.brewdogdiy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.notnotme.brewdogdiy.ui.Screen
import com.notnotme.brewdogdiy.ui.beer.BeerScreen
import com.notnotme.brewdogdiy.ui.list.ListScreen
import com.notnotme.brewdogdiy.ui.start.StartScreen
import com.notnotme.brewdogdiy.ui.theme.BrewdogDIYTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun AppBar(navController: NavHostController) {
    val showBackIcon = navController.currentBackStackEntryAsState().value?.destination?.route != Screen.Start.route
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = if (showBackIcon) {
            {
                IconButton(
                    onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_back))
                }
            }
        } else null)
}

@Composable
@Preview(showBackground = true)
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
fun MainScreen() {
    BrewdogDIYTheme {
        Surface {
            Column {
                val navController = rememberNavController()
                val viewModel: MainActivityViewModel = hiltViewModel()

                 // TopAppBar will show a back navigation button if the app is displayed something else
                // than the start screen.
                AppBar(navController)

                // Need to be top level because recomposition kill it inside NevHost
                val pagingItems = remember {
                    viewModel.beerPager
                }.collectAsLazyPagingItems()

                // NavHost will manage navigation between views
                NavHost(
                    navController = navController,
                    startDestination = Screen.Start.route) {

                    // Start screen
                    composable(
                        route = Screen.Start.route) {
                        StartScreen({
                            navController.navigate(Screen.List.route)
                        }, {
                            navController.navigate(Screen.Beer.createRoute(0L))
                        })
                    }

                    // List screen
                    composable(
                        route = Screen.List.route) {

                        ListScreen(pagingItems) { selectedBeer ->
                            navController.navigate(Screen.Beer.createRoute(selectedBeer))
                        }
                    }

                    // Beer screen
                    composable(
                        route = Screen.Beer.route,
                        arguments = listOf(navArgument(Screen.Beer.BEER_ID_ARG) { defaultValue = 0L })) { navBackStackEntry ->

                        val beerState = remember {
                            val beerId = navBackStackEntry.arguments?.getLong(Screen.Beer.BEER_ID_ARG) ?: 0L
                            viewModel.getBeerOrRandom(beerId)
                                .map {
                                    // Replace actual beer id
                                    navBackStackEntry.arguments?.putLong(Screen.Beer.BEER_ID_ARG, it.data?.id ?: 0L)
                                    it
                                }
                        }.collectAsState(null)

                        BeerScreen(beerState)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = false)
fun AppBarPreview() {
    BrewdogDIYTheme {
        val navController = rememberNavController()
        AppBar(navController)
    }
}
