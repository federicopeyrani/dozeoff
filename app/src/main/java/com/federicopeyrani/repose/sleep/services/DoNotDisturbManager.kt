package com.federicopeyrani.repose.sleep.services

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_PRIORITY
import android.app.NotificationManager.Policy
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DoNotDisturbManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    val isNotificationPolicyAccessGranted get() = notificationManager.isNotificationPolicyAccessGranted

    val setDnd get() = ::setDndUnsafe.takeIf { isNotificationPolicyAccessGranted }

    private fun setDndUnsafe() {
        val policy = Policy(Policy.PRIORITY_CATEGORY_ALARMS, 0, 0)
        notificationManager.setInterruptionFilter(INTERRUPTION_FILTER_PRIORITY)
        notificationManager.notificationPolicy = policy
    }
}