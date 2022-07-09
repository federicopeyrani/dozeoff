package com.federicopeyrani.repose.sleep

import com.federicopeyrani.repose.sleep.data.SleepClassifyEntity
import com.federicopeyrani.repose.sleep.data.SleepEventDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SleepRepository @Inject constructor(
    private val sleepEventDao: SleepEventDao
) {

    suspend fun insertSleepClassifies(sleepSegments: List<SleepClassifyEntity>) =
        sleepEventDao.insertSleepClassifies(sleepSegments)

    fun getLastSleepClassifyFlow() = sleepEventDao.getLastSleepClassifyFlow()
}
