package com.vboard.bp_recorder_app.utils.interfaces

import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable

interface ShowBPAdapterCallbacks {

    fun OnEditIconClick(bloodPressureTable: BloodPressureTable)

    fun OnItemClick(bloodPressureTable: BloodPressureTable)

}