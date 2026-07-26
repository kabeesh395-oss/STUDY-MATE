package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BenGlow
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.PrimaryText
import kotlin.math.cos
import kotlin.math.sin

enum class BenMood {
    IDLE,
    HAPPY,
    TEACHING,
    THINKING,
    CELEBRATING,
    SLEEPING,
    LEGEND
}

@Composable
fun BenAvatar(
    mood: BenMood = BenMood.IDLE,
    size: Dp = 80.dp,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "benAnimation")

    val floatY by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatY"
    )

    val auraScale by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraScale"
    )

    val rotationAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .size(size)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        // Holographic canvas graphics
        Canvas(
            modifier = Modifier
                .size(size)
                .scale(auraScale)
        ) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f + floatY)
            val radius = this.size.width * 0.38f

            // Outer Glowing Ring
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonGreen.copy(alpha = 0.6f), NeonCyan.copy(alpha = 0.3f), Color.Transparent),
                    center = center,
                    radius = radius * 1.5f
                ),
                center = center,
                radius = radius * 1.4f
            )

            // Tech Ring Orbiting Particles
            val particleCount = 4
            for (i in 0 until particleCount) {
                val angleRad = Math.toRadians((rotationAngle + i * (360 / particleCount)).toDouble())
                val pX = center.x + (radius * 1.25f) * cos(angleRad).toFloat()
                val pY = center.y + (radius * 1.25f) * sin(angleRad).toFloat()
                drawCircle(
                    color = if (i % 2 == 0) NeonGreen else NeonCyan,
                    radius = 3f,
                    center = Offset(pX, pY)
                )
            }

            // Head Metallic Glass Orb
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1E293B),
                        Color(0xFF0F172A)
                    )
                ),
                center = center,
                radius = radius
            )

            // Neon Border
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(NeonGreen, NeonCyan, ElectricBlue, NeonGreen),
                    center = center
                ),
                center = center,
                radius = radius,
                style = Stroke(width = 3.dp.toPx())
            )

            // Visor Glass Dark Panel
            val visorRectSize = Size(radius * 1.3f, radius * 0.8f)
            val visorTopLeft = Offset(center.x - visorRectSize.width / 2f, center.y - visorRectSize.height / 2f)
            drawRoundRect(
                brush = Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF0F172A))),
                topLeft = visorTopLeft,
                size = visorRectSize,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
            )

            drawRoundRect(
                color = NeonGreen.copy(alpha = 0.4f),
                topLeft = visorTopLeft,
                size = visorRectSize,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f),
                style = Stroke(width = 1.5.dp.toPx())
            )

            // Mood-based Digital Eye Expressions
            val eyeOffsetY = center.y - 2f
            val eyeSpacing = radius * 0.35f

            when (mood) {
                BenMood.HAPPY -> {
                    // Smiling Arc Eyes
                    val pathLeft = Path().apply {
                        moveTo(center.x - eyeSpacing - 12f, eyeOffsetY + 4f)
                        quadraticTo(center.x - eyeSpacing, eyeOffsetY - 10f, center.x - eyeSpacing + 12f, eyeOffsetY + 4f)
                    }
                    val pathRight = Path().apply {
                        moveTo(center.x + eyeSpacing - 12f, eyeOffsetY + 4f)
                        quadraticTo(center.x + eyeSpacing, eyeOffsetY - 10f, center.x + eyeSpacing + 12f, eyeOffsetY + 4f)
                    }
                    drawPath(pathLeft, NeonGreen, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
                    drawPath(pathRight, NeonGreen, style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))
                }
                BenMood.SLEEPING -> {
                    // Closed Eyes (Horizontal Bar)
                    drawLine(NeonCyan, Offset(center.x - eyeSpacing - 10f, eyeOffsetY), Offset(center.x - eyeSpacing + 10f, eyeOffsetY), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
                    drawLine(NeonCyan, Offset(center.x + eyeSpacing - 10f, eyeOffsetY), Offset(center.x + eyeSpacing + 10f, eyeOffsetY), strokeWidth = 4.dp.toPx(), cap = StrokeCap.Round)
                }
                BenMood.THINKING -> {
                    // Left normal eye, right blinking dot
                    drawCircle(NeonGreen, radius = 6f, center = Offset(center.x - eyeSpacing, eyeOffsetY))
                    drawCircle(NeonCyan, radius = 10f, center = Offset(center.x + eyeSpacing, eyeOffsetY))
                }
                BenMood.CELEBRATING, BenMood.LEGEND -> {
                    // Glowing Cyan / Green Stars / Diamonds
                    drawCircle(NeonGreen, radius = 8f, center = Offset(center.x - eyeSpacing, eyeOffsetY))
                    drawCircle(NeonGreen, radius = 8f, center = Offset(center.x + eyeSpacing, eyeOffsetY))
                    // Crown / Legend spark at top of head
                    if (mood == BenMood.LEGEND) {
                        drawCircle(NeonCyan, radius = 6f, center = Offset(center.x, center.y - radius * 1.2f))
                    }
                }
                else -> { // IDLE / TEACHING
                    // Bright Cyan Circular Pupils
                    drawCircle(NeonCyan, radius = 7f, center = Offset(center.x - eyeSpacing, eyeOffsetY))
                    drawCircle(NeonCyan, radius = 7f, center = Offset(center.x + eyeSpacing, eyeOffsetY))
                    // Pupil core
                    drawCircle(Color.White, radius = 3f, center = Offset(center.x - eyeSpacing, eyeOffsetY))
                    drawCircle(Color.White, radius = 3f, center = Offset(center.x + eyeSpacing, eyeOffsetY))
                }
            }
        }
    }
}

@Composable
fun BenSpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CardDark)
            .border(1.dp, BenGlow, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(NeonGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText,
                fontSize = 13.sp
            )
        }
    }
}
