package com.example.oamkhub.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.oamkhub.data.model.EventItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EventRepository {
    private val client = OkHttpClient()
    private val url = "https://oamk.fi/en/about-oamk/news-and-events/"

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchEvents(): List<EventItem> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        val html = response.body?.string() ?: return@withContext emptyList()
        val document = Jsoup.parse(html)

        val eventElements = document.select("div.post-column-card.post-column-card--event")

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        return@withContext eventElements.mapNotNull {
            try {
                val rawDate = it.select("div.post-column-card__details").text()
                val dateOnly = rawDate.substringBefore(" ") // e.g., "02.04.2025"
                val parsedDate = try {
                    LocalDate.parse(dateOnly, formatter)
                } catch (e: Exception) {
                    null
                }

                val title = it.select("h3.mb-3").text()
                val link = it.select("a.text-decoration-none").attr("href")
                val imageUrl = it.select("div.ratio img").attr("src")

                EventItem(
                    title = title,
                    link = link,
                    date = rawDate,
                    imageUrl = imageUrl,
                    parsedDate = parsedDate
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

}
