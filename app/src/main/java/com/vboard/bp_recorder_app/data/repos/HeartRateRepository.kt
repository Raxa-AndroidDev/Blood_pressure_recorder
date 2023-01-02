package com.vboard.bp_recorder_app.data.repos

import androidx.lifecycle.LiveData
import com.vboard.bp_recorder_app.data.database.daos.HeartRateDao
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable

class HeartRateRepository(private val heartRateDao: HeartRateDao) {

    // function to store Heart rate data in DB
    fun storeHRRecordDB(heartRateTable: HeartRateTable) {
        heartRateDao.storeHeartRateRecord(heartRateTable)
    }

    fun fetchHRRecordFromDB(): LiveData<List<HeartRateTable>> {
        return heartRateDao.fetchAllHeartRateRecords()
    }




    suspend fun updateHeartRate(heartRate: HeartRateTable) {
        heartRateDao.updateHeartRate(heartRate)
    }
}