package com.federicopeyrani.repose.sleep.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.location.SleepClassifyEvent

@Entity
data class SleepClassifyEntity(
    @PrimaryKey val timestampMillis: Long,
    val light: Int,
    val motion: Int,
    val confidence: Int
) {

    companion object {

        fun fromSleepClassifyEvent(event: SleepClassifyEvent) = SleepClassifyEntity(
            timestampMillis = event.timestampMillis,
            light = event.light,
            motion = event.motion,
            confidence = event.confidence
        )
    }
}
