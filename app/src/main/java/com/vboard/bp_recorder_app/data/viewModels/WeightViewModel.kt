package com.vboard.bp_recorder_app.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.data.repos.BodyWeightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeightViewModel(application: Application):AndroidViewModel(application) {

    val repository = BodyWeightRepository(DatabaseClass.getDBInstance(application.applicationContext).weightDao())




    fun storeWeightRecordInDB(weightTable: BodyWeightTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.storeWeightRecordDB(weightTable)
        }

    }

    fun showWeightRecordFromDB(): LiveData<List<BodyWeightTable>> {

        return repository.fetchWeightRecordFromDB()
    }


    fun updateWeightRecord(weightTable: BodyWeightTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateWeightRecord(weightTable)
        }
    }


    fun deleteSpecificWeightRecord(position:Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSpecificWeightRecord(position)

        }
    }





}