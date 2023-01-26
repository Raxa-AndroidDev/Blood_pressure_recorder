package com.vboard.bp_recorder_app.utils.interfaces


import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable

interface ShowHRAdapterCallbacks {

    fun OnEditIconClick(heartRateTable: HeartRateTable)

    fun OnItemClick(heartRateTable: HeartRateTable)

}