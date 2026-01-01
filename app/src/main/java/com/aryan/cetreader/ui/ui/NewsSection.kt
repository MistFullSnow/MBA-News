package com.aryan.cetreader.ui

enum class NewsSection(
    val title: String,
    val rssUrl: String
) {
    INDIA(
        "India",
        "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms"
    ),
    GLOBAL(
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
    TECH(
        "Technology",
        "https://timesofindia.indiatimes.com/rssfeeds/5880659.cms"
    ),
    ENTERTAINMENT(
        "Entertainment",
        "https://timesofindia.indiatimes.com/rssfeeds/1081479906.cms?x=1"
    )
}
