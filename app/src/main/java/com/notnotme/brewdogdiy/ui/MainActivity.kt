package com.notnotme.brewdogdiy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.notnotme.brewdogdiy.ui.theme.BrewdogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterialApi
@ExperimentalPagingApi
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrewdogTheme {
                MainScreen()
            }
        }
    }
}
