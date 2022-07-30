package com.federicopeyrani.dozeoff.sleep.services

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.federicopeyrani.dozeoff.sleep.SleepRepository
import com.federicopeyrani.dozeoff.sleep.data.SleepClassifyEntity
import com.google.android.gms.location.SleepClassifyEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SleepReceiver : BroadcastReceiver() {

    private val scope = MainScope()

    @Inject
    lateinit var doNotDisturbManager: DoNotDisturbManager

    @Inject
    lateinit var sleepRepository: SleepRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (!SleepClassifyEvent.hasEvents(intent)) {
            return
        }

        val sleepClassifyEntities = SleepClassifyEvent.extractEvents(intent)
            .map(SleepClassifyEntity.Companion::fromSleepClassifyEvent)
        val sleepConfidence = sleepClassifyEntities
            .map { it.confidence }
            .average()

        Log.d(TAG, "onReceive: confidence=$sleepConfidence")

        scope.launch {
            sleepRepository.insertSleepClassifies(sleepClassifyEntities)
        }

        if (sleepConfidence > CONFIDENCE_THRESHOLD) {
            doNotDisturbManager.setDnd?.invoke()
            Log.d(TAG, "onReceive: turning DND on")
        }
    }

    companion object {

        private const val TAG = "SleepReceiver"

        private const val CONFIDENCE_THRESHOLD = 75

        private val BROADCAST_FLAGS = FLAG_CANCEL_CURRENT or when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> FLAG_MUTABLE
            else -> 0
        }

        fun createSleepReceiverPendingIntent(context: Context): PendingIntent {
            val sleepIntent = Intent(context, SleepReceiver::class.java)
            return PendingIntent.getBroadcast(
                /* context = */ context,
                /* requestCode = */ 0,
                /* intent = */ sleepIntent,
                /* flags = */ BROADCAST_FLAGS
            )
        }
    }
}
