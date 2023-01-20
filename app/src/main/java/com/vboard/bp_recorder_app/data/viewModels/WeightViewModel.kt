package com.vboard.bp_recorder_app.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vboard.bp_recorder_app.data.Alarm
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.data.repos.BPRepository
import com.vboard.bp_recorder_app.data.repos.BodyWeightRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WeightViewModel(application: Application):AndroidViewModel(application) {

    val repository = BodyWeightRepository(DatabaseClass.getDBInstance(application.applicationContext).weightDao())
    val alarmsLiveData: LiveData<List<Alarm>> = repository.alarmsLiveData


    fun storeWeightRecordInDB(weightTable: BodyWeightTable){
        viewModelScope.launch(Dispatchers.IO){
            repository.storeWeightRecordDB(weightTable)
        }
    }


    fun showWeightRecordFromDB():LiveData<List<BodyWeightTable>>{
        return repository.fetchWeightRecordFromDB()
    }

    fun showDateWiseRecord(startDate: Date, endDate: Date):LiveData<List<BodyWeightTable>>{
        return repository.searchWeightRecordByDate(startDate,endDate)
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










    fun insert(alarm: Alarm) {
        viewModelScope.launch( Dispatchers.IO ){
            repository.insert(alarm)
        }
    }

    fun update(alarm: Alarm) {
        viewModelScope.launch( Dispatchers.IO ){
            repository.update(alarm)
        }
    }

    fun delete(alarmId: Int) {
        viewModelScope.launch( Dispatchers.IO ){
            repository.delete(alarmId)
        }
    }









}