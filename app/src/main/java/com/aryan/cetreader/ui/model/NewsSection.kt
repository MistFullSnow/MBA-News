package com.aryan.cetreader.ui.model

enum class NewsSection(
    val title: String,
    val rssUrl: String
) {
    INDIA(
        "India",
        "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms"
    ),
    WORLD(
        "Global",
        "https://timesofindia.indiatimes.com/rssfeeds/296589292.cms"
    ),
    BUSINESS(
        "Business",
        "https://timesofindia.indiatimes.com/rssfeeds/1898055.cms"
    ),
    SPORTS(
        "Sports",
        "https://timesofindia.indiatimes.com/rssfeeds/4719148.cms"
    ),
    ENTERTAINMENT(
        "Entertainment",
        "https://timesofindia.indiatimes.com/rssfeeds/1081479906.cms"
    )
}
