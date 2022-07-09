package com.federicopeyrani.repose.sleep.services

import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.SleepSegmentRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val isActivityRecognitionPermissionApproved
        get() = ContextCompat.checkSelfPermission(
            /* context = */ context,
            /* permission = */ ACTIVITY_RECOGNITION
        ) == PERMISSION_GRANTED

    val subscribe get() = ::subscribeUnsafe.takeIf { isActivityRecognitionPermissionApproved }

    @SuppressLint("MissingPermission")
    private fun subscribeUnsafe() {
        ActivityRecognition.getClient(context).requestSleepSegmentUpdates(
            SleepReceiver.createSleepReceiverPendingIntent(context),
            SleepSegmentRequest.getDefaultSleepSegmentRequest()
        )
    }
}