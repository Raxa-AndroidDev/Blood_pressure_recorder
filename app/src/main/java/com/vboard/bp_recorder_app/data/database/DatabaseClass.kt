package com.vboard.bp_recorder_app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vboard.bp_recorder_app.data.database.daos.BPDao
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable

@Database( entities = [BloodPressureTable::class], version = 1)
abstract class DatabaseClass :RoomDatabase() {

    abstract fun bpDao() : BPDao

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