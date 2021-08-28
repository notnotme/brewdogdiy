package com.notnotme.brewdogdiy.ui.update

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.notnotme.brewdogdiy.R
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.ui.common.ErrorMessageBox
import com.notnotme.brewdogdiy.ui.common.LoadingMessageBox
import com.notnotme.brewdogdiy.ui.common.TextButton
import com.notnotme.brewdogdiy.ui.common.TopAppBar
import com.notnotme.brewdogdiy.util.toLocalDate


@Composable
fun UpdateScreen(
    updating: Boolean,
    errorMessage: String?,
    downloadStatus: DownloadStatus?,
    onUpdateButtonClick: () -> Unit,
    backAction: (() -> Unit)? = null,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backAction = backAction
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                errorMessage != null -> {
                    ErrorMessageBox(
                        text = errorMessage,
                        space = 16.dp
                    )
                    Spacer(
                        modifier = Modifier.padding(16.dp)
                    )
                    TextButton(
                        text = stringResource(R.string.retry),
                        onClick = { onUpdateButtonClick() }
                    )
                }
                updating -> {
                    LoadingMessageBox(
                        text = stringResource(R.string.updating),
                        space = 16.dp
                    )
                    Spacer(
                        modifier = Modifier.padding(16.dp)
                    )
                    val beersCount = downloadStatus?.totalBeers ?: 0
                    Text(
                        text = stringResource(
                            R.string.downloading_beers_count_message,
                            beersCount
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                downloadStatus != null -> {
                    Text(
                        text = stringResource(
                            R.string.last_update_date,
                            downloadStatus.lastUpdate.toLocalDate()
                        )
                    )
                    Spacer(
                        modifier = Modifier.padding(16.dp)
                    )
                    TextButton(
                        text = stringResource(R.string.update_now),
                        onClick = { onUpdateButtonClick() }
                    )
                }
            }
        }
    }
}
