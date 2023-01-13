package com.vboard.bp_recorder_app.data.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable

@Dao
interface BodyWeightDao {

    // to store BodyWeight data in DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeBodyWeightRecord(bodyWeightTable: BodyWeightTable)

    // to update a specific BP record
    @Update
    fun UpdateSingleWeightRecord(weightTable: BodyWeightTable)




    // to fetch BodyWeight data from DB and display to user
    @Query("SELECT * FROM BodyWeightTable")
    fun fetchAllBodyWeightRecords(): LiveData<List<BodyWeightTable>>

    // to search BodyWeight data by using data filter
    @Query("SELECT * FROM BodyWeightTable WHERE date =:date ")
    fun searchBodyWeightRecordByDate(date: String): LiveData<List<BodyWeightTable>>

    // to search BodyWeight data by using tag filter
    @Query("Select * From BodyWeightTable Where label =:label")
    fun searchBodyWeightRecordByTag(label: String): LiveData<List<BodyWeightTable>>



    // to delete a single BodyWeight record
    @Query("DELETE from BodyWeightTable where id =:position ")
    fun deleteSingleBodyWeightRecord(position: Int)

    // to delete the whole BodyWeight table at once
    @Delete
    fun DeleteAllBodyWeightRecords(bodyWeightTable: BodyWeightTable)
}