package com.vboard.bp_recorder_app.data.database.db_tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class HeartRateTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val time: String,
    val DateAndTime: String,

    val BPM: String,
    val label: String
):Parcelable
