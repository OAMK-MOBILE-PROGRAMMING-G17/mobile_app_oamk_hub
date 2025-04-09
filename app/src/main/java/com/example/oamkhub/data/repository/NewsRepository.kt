package com.example.oamkhub.data.repository

import com.example.oamkhub.data.model.news.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

class NewsRepository {
    private val client = OkHttpClient()
    private val newsUrl = "https://oamk.fi/en/about-oamk/news-and-events/"

    suspend fun fetchNews(): List<NewsItem> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(newsUrl).build()
        val response = client.newCall(request).execute()

        val html = response.body?.string() ?: return@withContext emptyList()
        val document = Jsoup.parse(html)

        val newsElements = document.select("div.post-column-card")
        return@withContext newsElements.mapNotNull {
            try {
                val title = it.select("h3").text()
                val link = it.select("a").attr("href")
                val dateRawDetails = it.select("div.mb-2").text()
                val date = dateRawDetails.split("/").first().trim()
                val imageUrl = it.select("div.ratio img").attr("src")

                NewsItem(
                    title = title,
                    link = link,
                    date = date,
                    imageUrl = imageUrl
                )
            } catch (e: Exception) {
                null
            }
        }

    }
}
