package com.aryan.cetreader.ui.rss

import com.aryan.cetreader.ui.model.Article
import java.net.HttpURLConnection
import java.net.URL

class RssRepository {

    private val parser = RssParser()

    fun fetchRss(url: String, source: String): List<Article> {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connectTimeout = 10000
        connection.readTimeout = 10000
        connection.requestMethod = "GET"

        connection.connect()

        return connection.inputStream.use {
            parser.parse(it, source)
        }
    }
}
