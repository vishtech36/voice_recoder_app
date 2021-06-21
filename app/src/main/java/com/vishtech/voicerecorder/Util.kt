package com.vishtech.voicerecorder

import android.util.TimeUtils
import java.util.*
import java.util.concurrent.TimeUnit

object Util {

    fun getTimeAgo(duration: Long): String{

        val now = Date()
        val timeInMills = now.time - duration

        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(timeInMills)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(timeInMills)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(timeInMills)
        val days: Long = TimeUnit.MILLISECONDS.toDays(timeInMills)

        if(seconds < 60) {
            return "just now"
        } else if(minutes == 1L) {
            return "a minute ago"
        } else if(minutes in 2..59){
            return "$minutes minutes age";
        } else if(hours == 1L){
            return "an hour ago"
        } else if(hours in 2..23){
            return "$hours hours ago"
        } else if(days == 1L){
            return "a day ago"
        } else {
            return "$days days ago"
        }
    }

}