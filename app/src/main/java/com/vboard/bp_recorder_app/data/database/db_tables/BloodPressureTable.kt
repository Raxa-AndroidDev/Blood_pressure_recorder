package com.vboard.bp_recorder_app.data.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BloodPressureTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val time: String,
    val DateAndTime: String,
    val systolic: Int,
    val diaSystolic: Int,
    val pulse: Int,
    val label: String
)
