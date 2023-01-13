package com.vboard.bp_recorder_app.ui.weight

import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable

interface WeightAdapterCallbacks {

    fun OnEditIconClick(weightTable: BodyWeightTable)

    fun OnItemClick(weightTable: BodyWeightTable)
}