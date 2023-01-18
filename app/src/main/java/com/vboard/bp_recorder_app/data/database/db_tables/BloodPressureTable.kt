package com.vboard.bp_recorder_app.data.database.db_tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
 data class BloodPressureTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: Long,
    val time: String,
    val fulldate: String,
    val systolic: Int,
    val diaSystolic: Int,
    val pulse: Int,
    val tag: String
):Parcelable
