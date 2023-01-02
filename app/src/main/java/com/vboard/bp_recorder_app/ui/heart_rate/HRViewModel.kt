package com.vboard.bp_recorder_app.ui.heart_rate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.vboard.bp_recorder_app.data.database.DatabaseClass
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable
import com.vboard.bp_recorder_app.data.repos.HeartRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HRViewModel(application: Application):AndroidViewModel(application) {

    val readAllHeartRateData: LiveData<List<HeartRateTable>>
    private val repository: HeartRateRepository
    init {
        val hrDao = DatabaseClass.getDBInstance(application).hrDao()
        repository = HeartRateRepository(hrDao)
        readAllHeartRateData = repository.fetchHRRecordFromDB()
    }

    fun addHeartRate(heartRateModel: HeartRateTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.storeHRRecordDB(heartRateModel)
        }
    }

    fun updateHeartRate(heartRateModel: HeartRateTable) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateHeartRate(heartRateModel)
        }
    }



}