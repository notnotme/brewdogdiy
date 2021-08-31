package com.notnotme.brewdogdiy.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.ui.common.ImageButton
import com.notnotme.brewdogdiy.ui.common.TopAppBar
import com.notnotme.brewdogdiy.ui.theme.Typography

@Composable
fun HomeScreen(
    navigateToCatalog: () -> Unit,
    navigateToRandom: () -> Unit,
    navigateToAbv: () -> Unit,
    navigateToIbu: () -> Unit,
    navigateToFby: () -> Unit,
    onUpdateClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = { onUpdateClick() }) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = stringResource(R.string.update)
                        )
                    }
                }
            )
        }
    ) {
        Box {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(150.dp, 100.dp),
                painter = painterResource(R.drawable.ic_bottle),
                contentDescription = "Background",
                contentScale = ContentScale.Fit,
                alpha = ContentAlpha.high,
                colorFilter = ColorFilter.tint(Color.LightGray, BlendMode.SrcAtop)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.h2,
                    text = stringResource(R.string.home_title),
                    textAlign = TextAlign.Start
                )
                Column(
                    modifier = Modifier.weight(1.0f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        ImageButton(
                            modifier = Modifier.weight(1.0f),
                            text = stringResource(R.string.section_browse_all),
                            maxLines = 2,
                            imageVector = Icons.Default.ViewList,
                            onClick = { navigateToCatalog() }
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        ImageButton(
                            modifier = Modifier.weight(1.0f),
                            text = stringResource(R.string.section_food_pairing),
                            maxLines = 2,
                            imageVector = Icons.Default.DinnerDining,
                            onClick = { /* TODO */ }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        ImageButton(
                            modifier = Modifier.weight(1.0f),
                            text = stringResource(R.string.section_alcohol_by_volume),
                            maxLines = 2,
                            imageVector = Icons.Default.Thermostat,
                            onClick = { navigateToAbv() }
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        ImageButton(
                            modifier = Modifier.weight(1.0f),
                            text = stringResource(R.string.section_bitterness_unit),
                            maxLines = 2,
                            imageVector = Icons.Default.FormatBold,
                            onClick = { navigateToIbu() }
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        ImageButton(
                            modifier = Modifier.weight(1.0f),
                            text = stringResource(R.string.section_i_dont_know),
                            maxLines = 2,
                            imageVector = Icons.Default.AutoAwesome,
                            onClick = { navigateToRandom() }
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        ImageButton(
                            modifier = Modifier.weight(1.0f),
                            text = stringResource(R.string.section_first_brewed),
                            maxLines = 2,
                            imageVector = Icons.Default.ChildFriendly,
                            onClick = { navigateToFby() }
                        )
                    }
                }
            }
        }
    }
}
