package com.notnotme.brewdogdiy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import com.notnotme.brewdogdiy.ui.Screen
import com.notnotme.brewdogdiy.ui.beer.BeerScreen
import com.notnotme.brewdogdiy.ui.beer.BeerScreenViewModel
import com.notnotme.brewdogdiy.ui.list.ListScreen
import com.notnotme.brewdogdiy.ui.list.ListScreenViewModel
import com.notnotme.brewdogdiy.ui.start.StartScreen
import com.notnotme.brewdogdiy.ui.theme.BrewdogDIYTheme
import com.notnotme.brewdogdiy.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

                 // TopAppBar will show a back navigation button if the app is displayed something else
                 // than the start screen.
                AppBar(navController)

                 // NavHost will manage navigation between views
                NavHost(
                    navController = navController,
                    startDestination = Screen.Start.route) {

                    // Start screen
                    composable(
                        route = Screen.Start.route) {
                        StartScreen { navigateTo ->
                            navController.navigate(navigateTo.route)
                        }
                    }

                    // List screen
                    composable(
                        route = Screen.List.route) {

                        // Get the beerPager and pass it to the List screen
                        val viewModel: ListScreenViewModel = hiltViewModel()
                        val lazyPaging = viewModel.beerPager.collectAsLazyPagingItems()

                        ListScreen(lazyPaging) { selectedBeer ->
                            navController.navigate(Screen.Beer.createRoute(selectedBeer))
                        }
                    }

                    // Beer screen
                    composable(
                        route = Screen.Beer.route,
                        arguments = listOf(navArgument(Screen.Beer.BEER_ID_ARG) { defaultValue = 0L })) {

                        // Take the arguments and let's get a beer
                        val viewModel: BeerScreenViewModel = hiltViewModel()
                        val beerState = remember {
                                when (val beerId =
                                    it.arguments?.getLong(Screen.Beer.BEER_ID_ARG) ?: 0) {
                                    0L -> viewModel.getRandomBeer()
                                    else -> viewModel.getBeer(beerId)
                                }
                            }.collectAsState(Resource.loading(null))

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
