package com.aryan.cetreader.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun FeaturedCard() {
    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(140.dp)
            .clickable { },
        elevation = CardDefaults.cardElevation(6.dp),
        shape = CardDefaults.shape
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Featured", fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Elon Musk says X will pay content creators more than YouTube",
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text("Source: Times of India • 1h ago")
        }
    }
}
