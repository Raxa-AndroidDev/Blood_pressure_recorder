package com.vboard.bp_recorder_app.data.repos

import androidx.lifecycle.LiveData
import com.vboard.bp_recorder_app.data.database.daos.BodyWeightDao
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable

class BodyWeightRepository(private val bodyWeightDao: BodyWeightDao) {

    // function to store Body Weight data in DB
    fun storeBPRecordDB(bodyWeightTable: BodyWeightTable) {
        bodyWeightDao.storeBodyWeightRecord(bodyWeightTable)
    }

    fun fetchBPRecordFromBP(): LiveData<List<BodyWeightTable>> {
        return bodyWeightDao.fetchAllBodyWeightRecords()
    }
}