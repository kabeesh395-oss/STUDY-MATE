package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

val BEN_VOICE_QUOTES = listOf(
    "🔥 Welcome back!",
    "👍 Super da! Ready to study?",
    "⚡ Let's finish today's goal.",
    "🚀 Keep going! You got this.",
    "🌟 You are improving every day.",
    "🧠 Need help? Tap me to ask AI Tutor!"
)

@Composable
fun FloatingBenWidget(
    onOpenBenTutor: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quoteIndex by remember { mutableStateOf(0) }
    var showBubble by remember { mutableStateOf(true) }
    var currentMood by remember { mutableStateOf(BenMood.HAPPY) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(7000)
            showBubble = false
            delay(400)
            quoteIndex = (quoteIndex + 1) % BEN_VOICE_QUOTES.size
            currentMood = when (quoteIndex % 4) {
                0 -> BenMood.HAPPY
                1 -> BenMood.TEACHING
                2 -> BenMood.CELEBRATING
                else -> BenMood.IDLE
            }
            showBubble = true
        }
    }

    Box(
        modifier = modifier.padding(bottom = 16.dp, end = 16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(horizontalAlignment = Alignment.End) {
            AnimatedVisibility(
                visible = showBubble,
                enter = fadeIn() + scaleIn(initialScale = 0.8f),
                exit = fadeOut() + scaleOut(targetScale = 0.8f)
            ) {
                BenSpeechBubble(
                    text = BEN_VOICE_QUOTES[quoteIndex],
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            BenAvatar(
                mood = currentMood,
                size = 64.dp,
                onClick = onOpenBenTutor
            )
        }
    }
}
