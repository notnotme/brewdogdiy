package com.notnotme.brewdogdiy

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.notnotme.brewdogdiy.ui.theme.BrewdogDIYTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
fun BrewdogDIY() {
    BrewdogDIYTheme {
        val navController = rememberNavController()
        Scaffold(
            topBar = { AppBar(navController = navController) }
        ) {
            NavGraph(
                navController = navController
            )
        }
    }
}

@Composable
fun AppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.START_ROUTE

    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        navigationIcon = if (currentRoute != MainDestinations.START_ROUTE) {
            {
                IconButton(
                    onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }
            }
        } else null)
}

@Composable
@Preview(showBackground = false)
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
fun AppBarPreview() {
    BrewdogDIYTheme {
        AppBar(navController = rememberNavController())
    }
}
