package com.example.newsapplication.utils;

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateConverter {
    companion object {
        fun stringToTime(dateTime: String): String {
            if (dateTime.isEmpty()) {
                return ""
            }
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.US)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(dateTime)
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy hh:mm a",Locale.US) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            return dateFormatter.format(value!!)
        }

        fun stringToEpoch(dateTime: String?): Long {
            if (dateTime.isNullOrEmpty()) {
                return 0
            }
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(dateTime)
            return value!!.time
        }
    }
}