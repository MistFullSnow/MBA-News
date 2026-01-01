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
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme


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
        enter = fadeIn(tween(300)) + slideInVertically { it / 6 }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
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
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                Text(
                    item.source,
                    fontWeight = FontWeight.Medium,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    item.title,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 3,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    item.time,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize
                )
            }
        }
    }
}



data class NewsItem(
    val title: String,
    val source: String,
    val time: String
)

