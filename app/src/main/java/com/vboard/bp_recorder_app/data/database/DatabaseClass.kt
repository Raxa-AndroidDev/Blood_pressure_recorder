package com.vboard.bp_recorder_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vboard.bp_recorder_app.data.Alarm
import com.vboard.bp_recorder_app.data.database.daos.BPDao
import com.vboard.bp_recorder_app.data.database.daos.BodyWeightDao
import com.vboard.bp_recorder_app.data.database.daos.HeartRateDao
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable

@Database( entities = [BloodPressureTable::class ,HeartRateTable::class,BodyWeightTable::class,Alarm::class], version = 1)
abstract class DatabaseClass :RoomDatabase() {

    abstract fun bpDao() : BPDao
    abstract fun hrDao() : HeartRateDao
    abstract fun weightDao() : BodyWeightDao

    companion object{
         var dbInstance:DatabaseClass? = null

        fun getDBInstance(context: Context):DatabaseClass{
            synchronized(this){
                if (dbInstance ==null){
                    dbInstance = Room.databaseBuilder(context,DatabaseClass::class.java,"DB").build()
                }
            }

            return dbInstance!!
        }
    }
}