package com.example.testcase

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.icu.util.Calendar

//TODO:Replace on database ;)
fun Context.storage(): SharedPreferences = this.getSharedPreferences("my_prefs", MODE_PRIVATE)

fun SharedPreferences.getLastTimestamp(): Long?
    = getLong(LAST_TIMESTAMP, DEFAULT_VALUE).takeIf { it != DEFAULT_VALUE }

fun SharedPreferences.getDelta(): Long?
        = getLong(DELTA_TIMESTAMP, DEFAULT_VALUE).takeIf { it != DEFAULT_VALUE }


fun SharedPreferences.storeCurrentTimestamp() {
    val currentTime = Calendar.getInstance().timeInMillis
    getLong(LAST_TIMESTAMP, -1L)
        .takeIf { it != DEFAULT_VALUE }
        ?.run { edit().putLong(DELTA_TIMESTAMP, currentTime - this).apply() }

    edit().putLong(LAST_TIMESTAMP, currentTime).apply()
}


private const val LAST_TIMESTAMP = "last_timestamp"
private const val DELTA_TIMESTAMP = "delta"
private const val DEFAULT_VALUE = -1L
