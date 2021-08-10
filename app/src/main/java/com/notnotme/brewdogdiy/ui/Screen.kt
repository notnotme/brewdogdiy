package com.notnotme.brewdogdiy.ui

/**
 * Screen definition and helper
 * @param route The route to use for a Screen
 */
sealed class Screen(val route: String) {

    object Start : Screen("start")

    object List : Screen("list")

    object Beer : Screen("beer/?={beerId}") {
        fun createRoute(beerId: Long) = route.replace("{beerId}", beerId.toString())
        const val BEER_ID_ARG = "beerId"
    }

}