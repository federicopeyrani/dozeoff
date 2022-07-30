package com.federicopeyrani.dozeoff.sleep

import com.federicopeyrani.dozeoff.sleep.data.SleepClassifyEntity
import com.federicopeyrani.dozeoff.sleep.data.SleepEventDao
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
