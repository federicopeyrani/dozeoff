package com.federicopeyrani.repose.main

import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.federicopeyrani.repose.main.MainViewModel.PermissionStatus.Denied
import com.federicopeyrani.repose.main.MainViewModel.PermissionStatus.Granted
import com.federicopeyrani.repose.sleep.services.ActivityManager
import com.federicopeyrani.repose.sleep.services.DoNotDisturbManager
import com.federicopeyrani.repose.ui.theme.ReposeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val doNotDisturbPermissionManager = registerForActivityResult(
        /* contract = */ ActivityResultContracts.StartActivityForResult()
    ) {
        updateNotificationPolicyAccessStatus()
    }

    private val activityRecognitionPermissionManager = registerForActivityResult(
        /* contract = */ ActivityResultContracts.RequestPermission(),
        /* callback = */ ::updateActivityRecognitionPermissionStatus
    )

    @Inject
    lateinit var doNotDisturbManager: DoNotDisturbManager

    @Inject
    lateinit var activityManager: ActivityManager

    private fun updateActivityRecognitionPermissionStatus(
        isGranted: Boolean = activityManager.isActivityRecognitionPermissionApproved
    ) {
        if (isGranted) {
            activityManager.subscribe?.invoke()
        }

        viewModel.isActivityRecognitionPermissionGranted = when {
            isGranted -> Granted
            else -> Denied
        }
    }

    private fun updateNotificationPolicyAccessStatus(
        isGranted: Boolean = doNotDisturbManager.isNotificationPolicyAccessGranted
    ) {
        viewModel.isNotificationPolicyAccessGranted = when {
            isGranted -> Granted
            else -> Denied
        }
    }

    private fun askActivityRecognitionPermission() {
        activityRecognitionPermissionManager.launch(ACTIVITY_RECOGNITION)
    }

    private fun askNotificationPolicyAccess() {
        doNotDisturbPermissionManager.launch(DO_NOT_DISTURB_SETTINGS_INTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ReposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface
                ) {
                    Main(
                        viewModel = viewModel,
                        onGrantActivityRecognitionClick = ::askActivityRecognitionPermission,
                        onGrantNotificationPolicyAccessClick = ::askNotificationPolicyAccess
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateActivityRecognitionPermissionStatus()
        updateNotificationPolicyAccessStatus()
    }

    companion object {

        private val DO_NOT_DISTURB_SETTINGS_INTENT =
            Intent("android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS")
    }
}
