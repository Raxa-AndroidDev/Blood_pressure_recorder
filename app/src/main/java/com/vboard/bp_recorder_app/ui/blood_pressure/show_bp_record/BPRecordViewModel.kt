package com.vboard.bp_recorder_app.ui.blood_pressure.show_bp_record

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vboard.bp_recorder_app.data.repos.BPRepository
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BPRecordViewModel(application: Application):AndroidViewModel(application) {

    val repository = BPRepository(DatabaseClass.getDBInstance(application.applicationContext).bpDao())



    fun StoreBPRecordInDB(bloodPressureTable: BloodPressureTable){
       viewModelScope.launch(Dispatchers.IO){
           repository.storeBPRecordDB(bloodPressureTable)
       }

    }


    fun ShowBPRecordFromDB():LiveData<List<BloodPressureTable>>{

       return repository.fetchBPRecordFromBP()
    }



}