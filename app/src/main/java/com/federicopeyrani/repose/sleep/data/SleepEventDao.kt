package com.federicopeyrani.repose.sleep.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSleepClassifies(sleepSegments: List<SleepClassifyEntity>)

    @Query("SELECT * FROM SleepClassifyEntity ORDER BY timestampMillis DESC")
    fun getLastSleepClassifyFlow(): Flow<SleepClassifyEntity?>
}
