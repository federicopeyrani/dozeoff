package com.federicopeyrani.repose.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.federicopeyrani.repose.main.MainViewModel.PermissionStatus.NotChecked
import com.federicopeyrani.repose.sleep.SleepRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    sleepRepository: SleepRepository
) : ViewModel() {

    var isActivityRecognitionPermissionGranted by mutableStateOf<PermissionStatus>(NotChecked)

    var isNotificationPolicyAccessGranted by mutableStateOf<PermissionStatus>(NotChecked)

    val lastSleepClassify = sleepRepository
        .getLastSleepClassifyFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    sealed class PermissionStatus {

        object NotChecked : PermissionStatus()

        object Granted : PermissionStatus()

        object Denied : PermissionStatus()
    }
}