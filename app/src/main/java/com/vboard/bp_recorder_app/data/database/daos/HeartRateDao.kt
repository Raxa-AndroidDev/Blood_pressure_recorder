package com.vboard.bp_recorder_app.data.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable
import java.util.*

@Dao
interface HeartRateDao {

    // to store HeartRate data in DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeHeartRateRecord(heartRateTable: HeartRateTable)

    // to fetch HeartRate data from DB and display to user
    @Query("SELECT * FROM HeartRateTable")
    fun fetchAllHeartRateRecords(): LiveData<List<HeartRateTable>>


    // to search BodyWeight data by using data filter
    @Query("SELECT * FROM HeartRateTable WHERE date BETWEEN :startDate AND :endDate")
    fun searchHRRecordByDate(startDate: Date, endDate: Date): LiveData<List<HeartRateTable>>


    // to search HeartRate data by using tag filter
    /*@Query("Select * From HeartRateTable Where label =:label")
    fun searchHeartRateRecordByTag(label: String): LiveData<List<HeartRateTable>>*/


    @Update
    fun updateHeartRate(heartRate: HeartRateTable)


    // to delete a single HeartRate record
    @Query("DELETE from HeartRateTable where id =:position ")
    fun deleteSingleHeartRateRecord(position: Int)

    // to delete the whole HeartRate table at once
    @Delete
    fun deleteAllHeartRateRecords(heartRateTable: HeartRateTable)
}