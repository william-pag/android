package com.example.pagandroid.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("SimpleDateFormat")
class Datetime {
    companion object {
        val shard = Datetime()
    }
    fun formatDateTime(datetime: String?, pattern: String = "dd-MM-yyyy"): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date: Date = inputDateFormat.parse(datetime ?: Date().toString()) ?: Date()
        val outputDateFormat = SimpleDateFormat(pattern)
        return outputDateFormat.format(date)
    }

    fun countDateToNow(datetime: String): Int {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val differenceInMilliseconds: Long = Date().time - inputDateFormat.parse(datetime).time
        return (differenceInMilliseconds / 86400000).toInt()
    }
}
