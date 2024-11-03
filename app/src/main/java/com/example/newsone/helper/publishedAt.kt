package com.example.newsone.helper

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun getRelativeTime(publishedAt: String): String {
    // Define the date format used in the API response
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")

    // Parse the publishedAt date string into a Date object
    return try {
        val date = dateFormat.parse(publishedAt)
        if (date != null) {
            // Convert the date to relative time span
            DateUtils.getRelativeTimeSpanString(
                date.time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            ).toString()
        } else {
            "Unknown time"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Unknown time"
    }
}
