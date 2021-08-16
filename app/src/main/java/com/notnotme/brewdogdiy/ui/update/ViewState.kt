package com.notnotme.brewdogdiy.ui.update

import com.notnotme.brewdogdiy.model.domain.DownloadStatus

/**
 * UpdateScreen view states
 */
data class ViewState(
    val updating: Boolean = false,
    val downloadStatus: DownloadStatus? = null,
    val errorMessage: String? = null
)
