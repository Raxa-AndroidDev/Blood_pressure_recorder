package com.vboard.bp_recorder_app.utils.interfaces

import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable

interface ShowWeightAdapterCallbacks {

    fun OnEditIconClick(weightTable: BodyWeightTable)

    fun OnItemClick(weightTable: BodyWeightTable)

}