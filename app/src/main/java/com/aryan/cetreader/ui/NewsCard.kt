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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.graphics.graphicsLayer



@Composable
fun NewsCard(item: NewsItem) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "cardScale"
    )

    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + slideInVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                },
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
}


if (article == null) return

Text("Featured", fontWeight = FontWeight.SemiBold)
Spacer(modifier = Modifier.height(6.dp))
Text(
    article.title,
    fontWeight = FontWeight.Bold,
    maxLines = 2
)
Spacer(modifier = Modifier.height(4.dp))
Text("${article.source} • ${article.pubDate}")
