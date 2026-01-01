package com.aryan.cetreader.data

import com.aryan.cetreader.ui.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import android.util.Xml
import java.net.URL

class NewsRepository {

    suspend fun fetchArticles(rssUrl: String): List<Article> =
        withContext(Dispatchers.IO) {

            val articles = mutableListOf<Article>()
            val parser = Xml.newPullParser()
            parser.setInput(URL(rssUrl).openStream(), null)

            var eventType = parser.eventType
            var title = ""
            var link = ""
            var pubDate = ""
            var source = ""

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "item" -> {
                                title = ""
                                link = ""
                                pubDate = ""
                                source = ""
                            }
                            "title" -> title = parser.nextText()
                            "link" -> link = parser.nextText()
                            "pubDate" -> pubDate = parser.nextText()
                            "source" -> source = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "item") {
                            articles.add(
                                Article(
                                    title = title,
                                    link = link,
                                    pubDate = pubDate,
                                    source = if (source.isBlank()) "Times of India" else source
                                )
                            )
                        }
                    }
                }
                eventType = parser.next()
            }
            articles
        }
}
