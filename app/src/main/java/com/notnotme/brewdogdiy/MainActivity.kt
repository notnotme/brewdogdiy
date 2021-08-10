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
@Preview(showBackground = true)
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
fun MainScreen() {
    BrewdogDIYTheme {

        val navController = rememberNavController()
        val showBackIcon = navController.currentBackStackEntryAsState()
            .value?.destination?.route != Screen.Start.route

        Surface {
            Column {
                 // TopAppBar will show a back navigation button if the app is displayed something else
                 // than the start screen.
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

                        // Get the beerPager lazy paging and pass it to the List screen
                        val viewModel: ListScreenViewModel = hiltViewModel()
                        val pagingData = remember {
                                viewModel.beerPager
                            }.collectAsLazyPagingItems()

                        ListScreen(pagingData) { selectedBeer ->
                            navController.navigate(Screen.Beer.createRoute(selectedBeer))
                        }
                    }

                    // Beer screen
                    composable(
                        route = Screen.Beer.route,
                        arguments = listOf(navArgument(Screen.Beer.BEER_ID_ARG) { defaultValue = 0L })) {

                        // Take the arguments and let's get a beer
                        val viewModel: BeerScreenViewModel = hiltViewModel()
                        val beerId = it.arguments?.getLong(Screen.Beer.BEER_ID_ARG)?:0
                        val beerState = remember {
                                when (beerId) {
                                    0L -> viewModel.getRandomBeer()
                                    else -> viewModel.getBeer(beerId)
                                }
                            }.collectAsState(null)

                        BeerScreen(beerState)
                    }
                }
            }
        }
    }
}
