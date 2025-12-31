package com.aryan.cetreader.ui

import com.aryan.cetreader.ui.model.Article
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FeaturedCard(article: Article?) {


    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val haptic = LocalHapticFeedback.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "featuredScale"
    )

    AnimatedVisibility(
        visible = true,
        enter = fadeIn(tween(500)) + slideInVertically(
            initialOffsetY = { -it / 4 }
        )
    ) {
        Card(
            modifier = Modifier
                .padding(12.dp)
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
            elevation = CardDefaults.cardElevation(6.dp),
            shape = CardDefaults.shape
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Featured", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    article.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text("${article.source} • ${article.pubDate}")

            }
        }
    }
}
