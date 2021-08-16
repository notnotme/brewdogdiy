package com.notnotme.brewdogdiy.ui.update

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.util.toLocalDate

@Composable
fun UpdateScreen() {
    val viewModel: UpdateScreenViewModel = hiltViewModel()
    val viewState by viewModel.state.collectAsState()

    UpdateScreenContent(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        updating =  viewState.updating,
        errorMessage = viewState.errorMessage,
        downloadStatus = viewState.downloadStatus,
        onUpdateButtonClick = { viewModel.queueUpdate() }
    )
}

@Composable
fun UpdateScreenContent(
    modifier: Modifier = Modifier,
    updating: Boolean,
    errorMessage: String?,
    downloadStatus: DownloadStatus?,
    onUpdateButtonClick: () -> Unit
) {
    Surface(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    errorMessage != null -> {
                        Image(
                            imageVector = Icons.Default.Error,
                            contentDescription = "",
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.error, BlendMode.SrcAtop),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = errorMessage,
                            textAlign = TextAlign.Center
                        )
                        Spacer(
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = { onUpdateButtonClick() }
                        ) {
                            Text(
                                text = stringResource(R.string.retry)
                            )
                        }
                    }
                    updating -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = stringResource(R.string.updating)
                        )
                        Spacer(
                            modifier = Modifier.padding(16.dp)
                        )

                        val beersCount = downloadStatus?.totalBeers ?: 0
                        Text(
                            text = stringResource(R.string.downloading_beers_count_message, beersCount)
                        )
                    }
                    downloadStatus != null -> {
                        Text(
                            text = stringResource(R.string.last_update_date, downloadStatus.lastUpdate.toLocalDate())
                        )
                        Spacer(
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = { onUpdateButtonClick() }
                        ) {
                            Text(
                                text = stringResource(R.string.update_now)
                            )
                        }
                    }
                }
            }
        }
    }
}
