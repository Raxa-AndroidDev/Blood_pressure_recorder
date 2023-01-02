package com.vboard.bp_recorder_app.utils.interfaces

import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable

interface ShowBPAdapterCallbacks {

    fun OnDeleteIconClick(position:Int)

    fun OnItemClick(bloodPressureTable: BloodPressureTable)
}