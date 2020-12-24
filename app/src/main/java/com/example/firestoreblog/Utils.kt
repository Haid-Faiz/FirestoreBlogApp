package com.example.firestoreblog

import android.text.format.DateUtils

class Utils {

    fun getTimeAgo(postTime: Long): String {
        // getting system current time
        val now = System.currentTimeMillis()
        // this wil give difference in string with ago added
        val timeAgo = DateUtils.getRelativeTimeSpanString(postTime, now, DateUtils.MINUTE_IN_MILLIS)

        return timeAgo.toString()
    }
}