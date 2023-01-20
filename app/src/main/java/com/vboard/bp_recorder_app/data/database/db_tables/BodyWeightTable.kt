package com.vboard.bp_recorder_app.data.database.db_tables

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class BodyWeightTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val date: Long,
    val time: String,
    val fulldate: String,
    val weight: Int,
    val height: Int,
    val bmi:String,
    val weightType:String,
    val tag: String
):Parcelable
