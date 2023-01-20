package com.vboard.bp_recorder_app.data.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vboard.bp_recorder_app.data.Alarm
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.repos.BPRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class BPRecordViewModel(application: Application) : AndroidViewModel(application) {

    val repository =
        BPRepository(DatabaseClass.getDBInstance(application.applicationContext).bpDao())
    val alarmsLiveData: LiveData<List<Alarm>> = repository.alarmsLiveData


    val pulseValue = MutableLiveData<Int>(75)


    fun getPulseNumberPickerValue(oldValue: Int, newValue: Int) {
        pulseValue.postValue(newValue)
    }


    fun StoreBPRecordInDB(bloodPressureTable: BloodPressureTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.storeBPRecordDB(bloodPressureTable)
        }

    }

    fun ShowBPRecordFromDB(): LiveData<List<BloodPressureTable>> {

        return repository.fetchBPRecordFromBP()
    }

    fun showDateWiseRecord(startDate: Date, endDate: Date): LiveData<List<BloodPressureTable>> {
        return repository.searchBPRecordByDate(startDate, endDate)
    }

    fun DeleteSpecificBPRecord(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteSpecificBPRecord(position)

        }
    }

    fun UpdateBPRecord(bloodPressureTable: BloodPressureTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBPRecord(bloodPressureTable)
        }
    }


    fun insert(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(alarm)
        }
    }

    fun update(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(alarm)
        }
    }

    fun delete(alarmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(alarmId)
        }
    }


}