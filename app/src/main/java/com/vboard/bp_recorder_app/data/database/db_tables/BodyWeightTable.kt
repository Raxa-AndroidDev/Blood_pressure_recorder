package com.vboard.bp_recorder_app.data.database.db_tables

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BodyWeightTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val date: String,
    val time: String,
    val DateAndTime: String,
    val weight: String,
    val height: String,
    val label: String
)
