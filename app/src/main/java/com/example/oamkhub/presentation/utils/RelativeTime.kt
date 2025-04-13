package com.example.oamkhub.presentation.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun getRelativeTimeSpan(createdAt: String): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    parser.timeZone = TimeZone.getTimeZone("UTC")
    val createdDate: Date = try {
        parser.parse(createdAt) ?: Date()
    } catch (_: ParseException) {
        Date()
    }

    val now = System.currentTimeMillis()
    val diff = now - createdDate.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60 -> "Posted just now"
        minutes < 60 -> "Posted $minutes minutes ago"
        hours < 24 -> "Posted $hours hours ago"
        else -> "Posted $days days ago"
    }
}
