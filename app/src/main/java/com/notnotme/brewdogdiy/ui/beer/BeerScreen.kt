package com.notnotme.brewdogdiy.ui.beer

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.Beer
import com.notnotme.brewdogdiy.ui.common.ErrorMessageBox
import com.notnotme.brewdogdiy.ui.common.LoadingMessageBox
import com.notnotme.brewdogdiy.ui.common.ParagraphBox
import com.notnotme.brewdogdiy.ui.common.SimpleAppBar
import com.notnotme.brewdogdiy.ui.theme.Typography
import com.notnotme.brewdogdiy.util.Resource
import com.notnotme.brewdogdiy.util.getYearAsString

@Composable
fun BeerScreen(
    beerResource: Resource<Beer>? = null,
    errorMessage: String? = null,
    backAction: () -> Unit
) {
    Scaffold(
        topBar = {
            SimpleAppBar(
                backAction = backAction
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colors.surface
        ) {
            if (errorMessage != null) {
                ErrorMessageBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    text = errorMessage,
                    space = 16.dp
                )
            } else {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    beerResource?.let {
                        when (beerResource.status) {
                            Resource.Companion.Status.Error -> { /* errorMessage handle this case */ }
                            Resource.Companion.Status.Loading -> {
                                LoadingMessageBox(
                                    modifier = Modifier.fillMaxSize(),
                                    text = stringResource(R.string.loading),
                                    space = 16.dp
                                )
                            }
                            Resource.Companion.Status.Success -> {
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
fun BeerInfo(beer: Beer) {
    val dividerColor = contentColorFor(LocalContentColor.current)
    Text(
        style = Typography.h3,
        text = beer.name,
        textAlign = TextAlign.Start
    )
    Spacer(modifier = Modifier
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
                beer.ibu.toInt()
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
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(32.dp)
    )
    ParagraphBox(
        modifier = Modifier.fillMaxWidth(),
        title = "This beer is",
        text = beer.description
    )
}
