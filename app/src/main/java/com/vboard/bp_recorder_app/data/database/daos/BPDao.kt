package com.vboard.bp_recorder_app.data.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable


@Dao
interface BPDao {

    // to store BP data in DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeBPRecord(bpTable: BloodPressureTable)

    // to fetch BP data from DB and display to user
    @Query("SELECT * FROM BloodPressureTable")
    fun fetchAllBPRecords(): LiveData<List<BloodPressureTable>>

    // to search BP data by using data filter
    @Query("SELECT * FROM BloodPressureTable WHERE date =:date ")
    fun searchBPRecordByDate(date: String): LiveData<List<BloodPressureTable>>

    // to search BP data by using tag filter
    @Query("Select * From BloodPressureTable Where label =:label")
    fun searchBPRecordByTag(label: String): LiveData<List<BloodPressureTable>>

    // to update a specific BP record
    @Query("UPDATE BloodPressureTable SET id =:position")
    fun UpdateSingleBPRecord(position: Int)

    // to delete a single BP record
    @Query("DELETE from BloodPressureTable where id =:position ")
    fun deleteSingleBPRecord(position: Int)

    // to delete the whole BP table at once
    @Delete
    fun DeleteAllBPRecords(bpTable: BloodPressureTable)
}