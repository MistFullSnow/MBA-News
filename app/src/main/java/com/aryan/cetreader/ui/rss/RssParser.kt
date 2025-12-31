package com.aryan.cetreader.ui.rss

import com.aryan.cetreader.ui.model.Article
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class RssParser {

    fun parse(inputStream: InputStream, sourceName: String): List<Article> {
        val articles = mutableListOf<Article>()

        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        parser.setInput(inputStream, null)

        var eventType = parser.eventType
        var currentTag = ""

        var title = ""
        var description = ""
        var link = ""
        var pubDate = ""

        while (eventType != XmlPullParser.END_DOCUMENT) {

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = parser.name
                }

                XmlPullParser.TEXT -> {
                    when (currentTag) {
                        "title" -> title = parser.text
                        "description" -> description = parser.text
                        "link" -> link = parser.text
                        "pubDate" -> pubDate = parser.text
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == "item") {
                        articles.add(
                            Article(
                                title = title,
                                description = description,
                                link = link,
                                source = sourceName,
                                pubDate = pubDate
                            )
                        )

                        title = ""
                        description = ""
                        link = ""
                        pubDate = ""
                    }
                    currentTag = ""
                }
            }
            eventType = parser.next()
        }

        return articles
    }
}
