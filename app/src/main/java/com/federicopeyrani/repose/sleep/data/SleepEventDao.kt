package com.federicopeyrani.repose.sleep.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SleepEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSleepClassifies(sleepSegments: List<SleepClassifyEntity>)

    @Query("SELECT * FROM SleepClassifyEntity")
    abstract fun getSleepClassifiesFlow(): Flow<List<SleepClassifyEntity>>

    @Query("SELECT * FROM SleepClassifyEntity ORDER BY timestampMillis DESC")
    abstract fun getLastSleepClassifyFlow(): Flow<SleepClassifyEntity?>
}