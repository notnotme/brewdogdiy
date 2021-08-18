package com.notnotme.brewdogdiy.ui.update

import android.app.Application
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.notnotme.brewdogdiy.model.domain.DownloadStatus
import com.notnotme.brewdogdiy.repository.BeerRepository
import com.notnotme.brewdogdiy.util.contentOrNull
import com.notnotme.brewdogdiy.work.UpdateWorker
import com.notnotme.brewdogdiy.work.UpdateWorker.Companion.toDownloadStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class UpdateScreenViewModel @Inject constructor(
    application: Application,
    private val beerRepository: BeerRepository,
) : ViewModel() {

    companion object {
        const val TAG = "UpdateScreenViewModel"
    }

    private val _state = MutableStateFlow(ViewState())
    val state: StateFlow<ViewState> get() = _state

    private val downloadStatus = MutableStateFlow<DownloadStatus?>(null)
    private val updating = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    private val workManager = WorkManager.getInstance(application.applicationContext)
    private val workerStatus = workManager.getWorkInfosByTagLiveData(UpdateWorker.TAG)
    private val workerObserver = Observer<List<WorkInfo>> {
        if (it.isNullOrEmpty()) {
            return@Observer
        }

        val workInfo = it[0]
        when (workInfo.state) {
            WorkInfo.State.RUNNING -> {
                val status = workInfo.progress.getString(UpdateWorker.KEY_UPDATE)?.toDownloadStatus()
                downloadStatus.value = status
            }
            WorkInfo.State.SUCCEEDED -> {
                val status = workInfo.outputData.getString(UpdateWorker.KEY_RESULT)?.toDownloadStatus()
                downloadStatus.value = status
                updating.value = false
            }
            WorkInfo.State.FAILED -> {
                errorMessage.value = workInfo.outputData.getString(UpdateWorker.KEY_FAILURE)
            }
            else -> { }
        }
    }

    init {
        // Observe the Worker status to update the corresponding UI states
        workerStatus.observeForever(workerObserver)

        // In case of no DownloadStatus previously saved we auto launch the update process
        viewModelScope.launch {
            try {
                val currentStatus = beerRepository.getDownloadStatus().first()
                if (currentStatus == null) {
                    queueUpdate()
                }
            } catch (exception: Exception) {
                errorMessage.value = exception.message?.contentOrNull() ?: "Unknown error"
            }
        }

        // Combines the latest value from each flow to generate a new ViewState
        viewModelScope.launch {
            combine(
                updating,
                downloadStatus,
                errorMessage
            ) { updating, downloadStatus, errorMessage ->
                ViewState(
                    updating = updating,
                    downloadStatus = downloadStatus,
                    errorMessage = errorMessage
                )
            }.catch { e ->
                Log.e(TAG, "Error: ${e.message}")
            }.collect {
                _state.value = it
            }
        }
    }

    override fun onCleared() {
        // Don't forget to remove the Worker observer
        workManager.cancelAllWorkByTag(UpdateWorker.TAG)
        workerStatus.removeObserver(workerObserver)
        super.onCleared()
    }

    /**
     * Submit a new update task to the UpdateWorker
     */
    fun queueUpdate() {
        errorMessage.value = null
        updating.value = true
        workManager.enqueueUniqueWork(
            UpdateWorker.TAG,
            ExistingWorkPolicy.KEEP,
            OneTimeWorkRequestBuilder<UpdateWorker>()
                .addTag(UpdateWorker.TAG)
                .build()
        )
    }

}
