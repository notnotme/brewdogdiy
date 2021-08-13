package com.notnotme.brewdogdiy.ui.beer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.theme.BrewdogDIYTheme
import com.notnotme.brewdogdiy.ui.theme.Typography
import com.notnotme.brewdogdiy.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@Composable
@ExperimentalCoroutinesApi
@ExperimentalCoilApi
fun BeerScreen(
    beer: Resource<Beer>?
) {
    beer?.let {
        when (it.status) {
            Resource.Companion.Status.Loading -> LoadingPage()
            Resource.Companion.Status.Success -> BeerPage(it.data!!)
            Resource.Companion.Status.Error -> ErrorPage(it.message!!)
        }
    }
}

@Composable
fun LoadingPage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                style = Typography.body1,
                text = stringResource(R.string.loading),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ErrorPage(message: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                imageVector = Icons.Default.Error,
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.error, BlendMode.SrcAtop),
                modifier = Modifier.size(64.dp)
            )

            Text(
                style = Typography.body1,
                text = message,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun BeerPage(beer: Beer) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            style = Typography.h5,
            text = beer.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
        )

        if (beer.tagLine.isNotBlank()) {
            Text(
                style = Typography.caption,
                text = beer.tagLine,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp, 0.dp, 16.dp, 16.dp)
            )
        }

        val painter = rememberImagePainter(
            data = beer.imageUrl ?: "",
            builder = {
                error(R.drawable.ic_broken_image)
                crossfade(true)
            })

        when (painter.state) {
            is ImagePainter.State.Loading -> CircularProgressIndicator(
                modifier = Modifier
                    .size(128.dp)
                    .padding(48.dp)
            )

            is ImagePainter.State.Empty,
            is ImagePainter.State.Error,
            is ImagePainter.State.Success -> Image(
                painter = painter,
                contentDescription = stringResource(R.string.beer_picture_desc),
                modifier = Modifier.size(128.dp)
            )
        }

        Text(
            style = Typography.body1,
            modifier = Modifier.padding(16.dp),
            text = beer.description,
            textAlign = TextAlign.Justify
        )
    }
}

// region Previews

@Composable
@Preview(showBackground = true)
@ExperimentalCoroutinesApi
fun LoadingPagePreview() {
    BrewdogDIYTheme {
        LoadingPage()
    }
}

@Composable
@Preview(showBackground = true)
@ExperimentalCoroutinesApi
fun ErrorPagePreview() {
    BrewdogDIYTheme {
        ErrorPage("Timeout")
    }
}


@Composable
@Preview(showBackground = true)
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
fun DefaultPreview() {
    BrewdogDIYTheme {
        val beer = Beer(
            id = 1337L,
            name = "Super Beer",
            tagLine = "THE beer",
            firstBrewed = Date(),
            description = "A beer that is called THE beer",
            imageUrl = "https://images.punkapi.com/v2/keg.png",
            abv = 0.0f,
            contributedBy = ""
        )
        BeerScreen(Resource.success(beer))
    }
}

// endregion previews
