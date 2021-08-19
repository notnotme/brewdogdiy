package com.notnotme.brewdogdiy.ui.beer

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.util.Resource
import java.util.*

@Composable
@Preview(showBackground = true)
fun BeerScreenPreview(

) {
    BeerScreen(
        beerResource = Resource.success(Beer(
            id = 1L,
            name = "A random beer",
            tagLine = "Yet another random beer",
            contributedBy = "romain",
            firstBrewed = Date(),
            description = "bla bla...",
            abv = 5.0f,
            ibu = 40.0f,
            imageUrl = null
        )),
        errorMessage = null,
        backAction = {}
    )
}

@Composable
@Preview(showBackground = true)
fun BeerScreenLoadingPreview(

) {
    BeerScreen(
        beerResource = Resource.loading(null),
        errorMessage = null,
        backAction = {}
    )
}

@Composable
@Preview(showBackground = true)
fun BeerScreenError(

) {
    BeerScreen(
        beerResource = Resource.error("", null),
        errorMessage = "Error message",
        backAction = {}
    )
}