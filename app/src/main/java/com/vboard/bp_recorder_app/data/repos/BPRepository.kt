package com.vboard.bp_recorder_app.data.repos

import androidx.lifecycle.LiveData
import com.vboard.bp_recorder_app.data.Alarm
import com.vboard.bp_recorder_app.data.database.daos.BPDao
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable

class BPRepository(private val bpDao: BPDao) {


    val alarmsLiveData: LiveData<List<Alarm>> = bpDao.getAlarms()

     // function to store BP data in DB at background thread
     fun storeBPRecordDB(bpTable: BloodPressureTable) {
        bpDao.storeBPRecord(bpTable)
    }

     fun fetchBPRecordFromBP(): LiveData<List<BloodPressureTable>>{
        return bpDao.fetchAllBPRecords()
    }



    fun searchBPRecordByDate(startDate:String, endDate:String):LiveData<List<BloodPressureTable>>{

        return bpDao.searchBPRecordByDate(startDate,endDate)
    }


    fun deleteSpecificBPRecord(position:Int){

        bpDao.deleteSingleBPRecord(position)
    }



    //repo for alarm

    suspend fun insert(alarm: Alarm) {
        bpDao.insert(alarm)
    }



    suspend fun update(alarm: Alarm) {
        bpDao.update(alarm)
    }

    suspend fun delete(alarmId: Int) {
        //bpDao.delete(alarmId)
    }


    fun updateBPRecord(bloodPressureTable: BloodPressureTable){
        bpDao.UpdateSingleBPRecord(bloodPressureTable)
    }
}