package com.vboard.bp_recorder_app.data.repos

import androidx.lifecycle.LiveData
import com.vboard.bp_recorder_app.data.database.daos.BodyWeightDao
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable

class BodyWeightRepository(private val bodyWeightDao: BodyWeightDao) {

    // function to store Body Weight data in DB
    fun storeWeightRecordDB(bodyWeightTable: BodyWeightTable) {
        bodyWeightDao.storeBodyWeightRecord(bodyWeightTable)
    }

    fun updateWeightRecord(bodyWeightTable: BodyWeightTable){
        bodyWeightDao.UpdateSingleWeightRecord(bodyWeightTable)
    }

    fun fetchWeightRecordFromDB(): LiveData<List<BodyWeightTable>> {
        return bodyWeightDao.fetchAllBodyWeightRecords()
    }

    fun deleteSpecificWeightRecord(position:Int){
            bodyWeightDao.deleteSingleBodyWeightRecord(position)
    }
}