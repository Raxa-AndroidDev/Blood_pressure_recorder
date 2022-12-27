package com.vboard.bp_recorder_app.data.repos

import androidx.lifecycle.LiveData
import com.vboard.bp_recorder_app.data.database.daos.BPDao
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable

class BPRepository(private val bpDao: BPDao) {

     // function to store BP data in DB at background thread
     fun storeBPRecordDB(bpTable: BloodPressureTable) {
        bpDao.storeBPRecord(bpTable)
    }

     fun fetchBPRecordFromBP(): LiveData<List<BloodPressureTable>>{
        return bpDao.fetchAllBPRecords()
    }
}