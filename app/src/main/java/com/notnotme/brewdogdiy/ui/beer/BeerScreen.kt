package com.notnotme.brewdogdiy.ui.beer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.navigationBarsPadding
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.*
import com.notnotme.brewdogdiy.ui.theme.Typography
import com.notnotme.brewdogdiy.util.Resource
import com.notnotme.brewdogdiy.util.getYearAsString

@Composable
@ExperimentalCoilApi
fun BeerScreen(
    beerResource: Resource<Beer>? = null,
    errorMessage: String? = null,
    backAction: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backAction = backAction
            )
        }
    ) {
        if (errorMessage != null) {
            ErrorMessageBox(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                text = errorMessage,
                space = 16.dp
            )
        } else {
            beerResource?.let {
                when (beerResource.status) {
                    Resource.Companion.Status.Error -> {
                        // errorMessage handle this case
                    }
                    Resource.Companion.Status.Loading -> {
                        LoadingMessageBox(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .navigationBarsPadding(),
                            text = stringResource(R.string.loading),
                            space = 16.dp
                        )
                    }
                    Resource.Companion.Status.Success -> {
                        val scrollState = rememberScrollState()
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .navigationBarsPadding()
                            ) {
                                BeerInfo(beerResource.data!!)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@ExperimentalCoilApi
fun BeerInfo(beer: Beer) {
    val dividerColor = contentColorFor(LocalContentColor.current)
    Text(
        style = Typography.h3,
        text = beer.name,
        textAlign = TextAlign.Start
    )
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
    )
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 4.dp,
        color = dividerColor
    )
    Text(
        modifier = Modifier.padding(0.dp, 4.dp),
        style = Typography.subtitle1,
        text = beer.tagLine,
        textAlign = TextAlign.Start
    )
    Divider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 4.dp,
        color = dividerColor
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(0.3f),
            style = Typography.body1,
            text = stringResource(
                R.string.abv,
                beer.abv
            ).uppercase(),
            textAlign = TextAlign.Start
        )
        Text(
            modifier = Modifier.weight(0.3f),
            style = Typography.body1,
            text = stringResource(
                R.string.ibu,
                beer.ibu
            ).uppercase(),
            textAlign = TextAlign.Start
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                modifier = Modifier.fillMaxWidth(0.4f),
                style = Typography.subtitle2,
                text = stringResource(
                    R.string.first_brewed,
                    beer.firstBrewed?.getYearAsString() ?: stringResource(R.string.unknown)
                ).uppercase(),
                textAlign = TextAlign.End
            )
        }
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    )
    ParagraphBox(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(R.string.this_beer_is),
        text = beer.description
    )
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    )
    TitledBox(
        modifier = Modifier.fillMaxWidth(),
        title = "Packaging"
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberImagePainter(
                data = beer.imageUrl ?: "",
                builder = {
                    error(R.drawable.ic_broken_image)
                    crossfade(true)
                })

            when (painter.state) {
                is ImagePainter.State.Loading -> CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )

                is ImagePainter.State.Empty,
                is ImagePainter.State.Error ->
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Image(
                            painter = painter,
                            contentDescription = stringResource(R.string.beer_picture_desc),
                            modifier = Modifier.fillMaxSize(),
                            colorFilter = ColorFilter.tint(Color.LightGray, BlendMode.SrcAtop)
                        )
                    }
                is ImagePainter.State.Success ->
                    Image(
                        painter = painter,
                        contentDescription = stringResource(R.string.beer_picture_desc),
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}
