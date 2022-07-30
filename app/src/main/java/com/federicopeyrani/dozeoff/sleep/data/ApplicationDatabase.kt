package com.federicopeyrani.dozeoff.sleep.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        SleepClassifyEntity::class
    ],
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {

    abstract fun getSleepEventDao(): SleepEventDao
}
