package com.vboard.bp_recorder_app.data.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vboard.bp_recorder_app.data.Alarm
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import java.util.*


@Dao
interface BPDao {

    // to store BP data in DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeBPRecord(bpTable: BloodPressureTable)

    // to fetch BP data from DB and display to user
    @Query("SELECT * FROM BloodPressureTable")
    fun fetchAllBPRecords(): LiveData<List<BloodPressureTable>>

    // to update a specific BP record
    @Update
    fun UpdateSingleBPRecord(bloodPressureTable: BloodPressureTable)


    // to search BP data by using date filter
    @Query("SELECT * FROM BloodPressureTable WHERE date BETWEEN :startDate AND :endDate")
     fun searchBPRecordByDate(startDate: Date, endDate: Date): LiveData<List<BloodPressureTable>>


    // to search BP data by using tag filter
    @Query("Select * From BloodPressureTable Where tag =:label")
    fun searchBPRecordByTag(label: String): LiveData<List<BloodPressureTable>>


    // to delete a single BP record
    @Query("DELETE from BloodPressureTable where id =:position ")
    fun deleteSingleBPRecord(position: Int)

    // to delete the whole BP table at once
    @Delete
    fun DeleteAllBPRecords(bpTable: BloodPressureTable)







    @Update
    fun update(alarm: Alarm?)

    @Query("SELECT * FROM alarm_table ORDER BY alarmId DESC")
    fun getAlarms(): LiveData<List<Alarm>>

    //Alarm methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alarm: Alarm?)

    @Query("DELETE FROM alarm_table")
    fun deleteAll()

}