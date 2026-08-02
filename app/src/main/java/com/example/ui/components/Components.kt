package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ripple
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardBorderDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MutedText
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SurfaceDark

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && onClick != null) 0.97f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "cardScale"
    )

    val shape = RoundedCornerShape(cornerRadius)
    Surface(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .border(1.dp, if (isPressed) ElectricBlue else borderColor, shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(bounded = true, color = ElectricBlue)
                    ) { onClick() }
                } else Modifier
            ),
        shape = shape,
        color = backgroundColor,
        tonalElevation = 0.dp
    ) {
        content()
    }
}

@Composable
fun ProgressRing(
    percentage: Int,
    modifier: Modifier = Modifier,
    radius: Dp = 48.dp,
    strokeWidth: Dp = 8.dp
) {
    val animatedProgress by animateFloatAsState(
        targetValue = percentage / 100f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Box(modifier = modifier.size(radius * 2), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Background track
            drawCircle(
                color = Color(0x0DFFFFFF),
                radius = size.minDimension / 2 - strokeWidth.toPx() / 2,
                style = Stroke(width = strokeWidth.toPx())
            )
            // Progress arc
            drawArc(
                color = ElectricBlue,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = PrimaryText,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun AnimatedBarChart(
    data: List<Pair<String, Float>>, // Day name, hours ratio (0.0 to 1.0)
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEachIndexed { index, (day, ratio) ->
            val animatedHeight by animateFloatAsState(
                targetValue = ratio,
                animationSpec = tween(durationMillis = 800, delayMillis = index * 60, easing = FastOutSlowInEasing),
                label = "barHeight"
            )

            val isHighlight = index == 2 || ratio >= 0.85f // Highlight prominent days

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .height(64.dp * animatedHeight + 16.dp)
                        .fillMaxWidth(0.6f)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(if (isHighlight) ElectricBlue else Color(0x0DFFFFFF))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = day.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isHighlight) ElectricBlue else MutedText
                )
            }
        }
    }
}

@Composable
fun ShimmerLoading(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            SurfaceDark,
            CardDark,
            SurfaceDark
        ),
        start = Offset(translateAnim - 200f, translateAnim - 200f),
        end = Offset(translateAnim, translateAnim)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(brush)
    )
}

@Composable
fun SkeletonCard(modifier: Modifier = Modifier) {
    GlassCard(
        modifier = modifier.fillMaxWidth(),
        cornerRadius = 20.dp,
        backgroundColor = CardDark,
        borderColor = GlassBorder
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ShimmerLoading(modifier = Modifier.fillMaxWidth(0.6f).height(18.dp))
            Spacer(modifier = Modifier.height(10.dp))
            ShimmerLoading(modifier = Modifier.fillMaxWidth(0.9f).height(14.dp))
            Spacer(modifier = Modifier.height(6.dp))
            ShimmerLoading(modifier = Modifier.fillMaxWidth(0.4f).height(12.dp))
        }
    }
}

@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "typingDots")

    val dot1Scale by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )

    val dot2Scale by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, delayMillis = 120, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )

    val dot3Scale by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, delayMillis = 240, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BrandedLoadingIndicator(size = 22.dp, strokeWidth = 2.dp, logoSize = 12.dp)
        Text(
            text = "Ben is thinking...",
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText,
            fontSize = 11.sp
        )
    }
}

@Composable
fun BrandedLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    strokeWidth: Dp = 2.5.dp,
    logoSize: Dp = (size.value * 0.52f).dp
) {
    val transition = rememberInfiniteTransition(label = "brandedLoadingRotation")
    val rotationAngle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringRotation"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Subtle electric-blue glow beneath the logo (10-15% opacity)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.18f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.toPx() * 0.55f
                )
            )
        }

        // Thin electric-blue circular ring that rotates smoothly clockwise
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { rotationZ = rotationAngle }
        ) {
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        ElectricBlue.copy(alpha = 0.05f),
                        ElectricBlue,
                        ElectricBlue.copy(alpha = 0.05f)
                    )
                ),
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        // Bold white lightning-shaped "S" logo (completely still)
        Image(
            painter = painterResource(id = R.drawable.ic_lightning_s_logo),
            contentDescription = "Loading",
            modifier = Modifier.size(logoSize)
        )
    }
}

@Composable
fun BrandedLoadingScreen(
    modifier: Modifier = Modifier,
    message: String? = null
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BrandedLoadingIndicator(
                size = 80.dp,
                strokeWidth = 3.dp,
                logoSize = 42.dp
            )
            if (!message.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = SecondaryText,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun AnimatedStreakFlame(streakDays: Int, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "flamePulse")
    val scale by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flameScale"
    )

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(FlameOrange.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🔥", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$streakDays Days ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )
        Text(
            text = "Streak",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedText
        )
    }
}

@Composable
fun BottomNavBar(
    currentScreen: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("HOME", "Home", Icons.Default.Home),
        NavItem("QUESTION_BANK", "Library", Icons.Default.Folder),
        NavItem("AI_CHAT", "AI Tutor", Icons.Default.Chat),
        NavItem("EXAM_MODE", "Tests", Icons.Default.AutoAwesome),
        NavItem("CAREER_HUB", "Career", Icons.Default.Work),
        NavItem("PROFILE", "Profile", Icons.Default.Person)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, GlassBorder, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .navigationBarsPadding(),
        color = BgDark.copy(alpha = 0.88f),
        tonalElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentScreen == item.route

                val iconScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.15f else 1.0f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
                    label = "navIconScale"
                )

                val iconColor by animateColorAsState(
                    targetValue = if (isSelected) ElectricBlue else SecondaryText,
                    animationSpec = tween(200),
                    label = "navIconColor"
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) ElectricBlue.copy(alpha = 0.12f) else Color.Transparent)
                        .clickable { onNavigate(item.route) }
                        .padding(vertical = 6.dp)
                        .testTag("nav_item_${item.route.lowercase()}")
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier.size(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = iconColor,
                                modifier = Modifier
                                    .size(20.dp)
                                    .scale(iconScale)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) ElectricBlue else SecondaryText,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .width(if (isSelected) 14.dp else 0.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(if (isSelected) ElectricBlue else Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}

data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun VoicePlayerBar(
    text: String,
    isPlaying: Boolean,
    speed: Float,
    onTogglePlay: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        cornerRadius = 24.dp,
        borderColor = GlassBorder,
        backgroundColor = SurfaceDark
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(ElectricBlue)
                        .clickable { onTogglePlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Voice Play",
                        tint = PrimaryText
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isPlaying) "🔊 Voice AI Reading..." else "🎧 Voice AI Audio Tutor",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = text.take(35) + if (text.length > 35) "..." else "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 11.sp,
                        color = SecondaryText
                    )
                }
            }

            // Speed Selector pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardDark)
                    .clickable {
                        val nextSpeed = when (speed) {
                            1.0f -> 1.25f
                            1.25f -> 1.5f
                            1.5f -> 2.0f
                            else -> 1.0f
                        }
                        onSpeedChange(nextSpeed)
                    }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${speed}x Speed",
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricBlueLight,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

