package com.example.oamkhub.data.model

import java.time.LocalDate

data class EventItem(
    val title: String,
    val link: String,
    val date: String,
    val imageUrl: String? = null,
    val parsedDate: LocalDate? = null
)