package com.aryan.cetreader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.tween


@Composable
fun NewsCard(item: NewsItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(item.source, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.title, fontWeight = FontWeight.Bold, maxLines = 3)
            Spacer(modifier = Modifier.height(6.dp))
            Text(item.time)
        }
    }
}
data class NewsItem(
    val title: String,
    val source: String,
    val time: String
)

val fakeNews = listOf(
    NewsItem("India launches indigenous Pralay missiles", "NDTV", "1h ago"),
    NewsItem("Thrown out of moving car at 90 kmph", "Hindustan Times", "2h ago"),
    NewsItem("UP home welcomes same-sex bride", "India Today", "3h ago"),
    NewsItem("Bangladesh ex-PM laid to rest", "NDTV World", "4h ago"),
    NewsItem("Tech layoffs slow down globally", "Economic Times", "5h ago"),
    NewsItem("New space policy announced", "The Hindu", "6h ago")
)
