package com.vboard.bp_recorder_app.data.repos

import androidx.lifecycle.LiveData
import com.vboard.bp_recorder_app.data.Alarm
import com.vboard.bp_recorder_app.data.database.daos.BodyWeightDao
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import java.util.*

class BodyWeightRepository(private val bodyWeightDao: BodyWeightDao) {

    val alarmsLiveData: LiveData<List<Alarm>> = bodyWeightDao.getAlarms()

    // function to store Body Weight data in DB
    fun storeWeightRecordDB(bodyWeightTable: BodyWeightTable) {
        bodyWeightDao.storeBodyWeightRecord(bodyWeightTable)
    }

    fun fetchWeightRecordFromDB(): LiveData<List<BodyWeightTable>> {
        return bodyWeightDao.fetchAllBodyWeightRecords()
    }

    fun updateWeightRecord(bodyWeightTable: BodyWeightTable) {
        bodyWeightDao.UpdateSingleWeightRecord(bodyWeightTable)
    }

    fun searchWeightRecordByDate(startDate: Date, endDate: Date): LiveData<List<BodyWeightTable>> {
        return bodyWeightDao.searchBodyWeightRecordByDate(startDate, endDate)
    }


    fun deleteSpecificWeightRecord(position: Int) {
        bodyWeightDao.deleteSingleBodyWeightRecord(position)
    }


    //repo for alarm

    suspend fun insert(alarm: Alarm) {
        bodyWeightDao.insert(alarm)
    }


    suspend fun update(alarm: Alarm) {
        bodyWeightDao.update(alarm)
    }

    suspend fun delete(alarmId: Int) {
        //bpDao.delete(alarmId)
    }


}