package com.notnotme.brewdogdiy.ui.update

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import java.util.*

@Composable
@Preview(showBackground = true)
fun UpdateScreenUpdate() {
    UpdateScreenContent(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        updating = true,
        downloadStatus = DownloadStatus(
            id = 0L,
            isFinished = false,
            lastUpdate = Date(),
            totalBeers = 84
        ),
        errorMessage = null,
        onUpdateButtonClick = {}
    )
}

@Composable
@Preview(showBackground = true)
fun UpdateScreenUpdated() {
    UpdateScreenContent(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        updating = false,
        downloadStatus = DownloadStatus(
            id = 0L,
            isFinished = false,
            lastUpdate = Date(),
            totalBeers = 84
        ),
        errorMessage = null,
        onUpdateButtonClick = {}
    )
}

@Composable
@Preview(showBackground = true)
fun UpdateScreenError() {
    UpdateScreenContent(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        updating = false,
        downloadStatus = null,
        errorMessage = "Device offline",
        onUpdateButtonClick = {}
    )
}
