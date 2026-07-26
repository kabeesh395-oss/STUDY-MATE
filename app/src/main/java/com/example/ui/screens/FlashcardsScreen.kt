package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun FlashcardsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val flashcardsList by viewModel.flashcards.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val currentCard = if (flashcardsList.isNotEmpty()) flashcardsList.getOrNull(currentIndex) else null

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "cardFlip"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateTo("HOME") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CardDark)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Interactive Flashcards",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )
                            Text(
                                text = "Spaced Repetition Engine",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SecondaryText,
                                fontSize = 12.sp
                            )
                        }
                    }

                    if (currentCard != null) {
                        IconButton(
                            onClick = { viewModel.toggleFlashcardBookmark(currentCard) }
                        ) {
                            Icon(
                                imageVector = if (currentCard.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (currentCard.isBookmarked) ElectricBlue else SecondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Indicator
                Text(
                    text = "Card ${currentIndex + 1} of ${flashcardsList.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricBlueLight,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Flip Card Container
                if (currentCard != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 8 * density
                            }
                            .clickable { isFlipped = !isFlipped }
                            .testTag("flashcard_flip_container"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = CardDark),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, GlassBorder)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotation <= 90f) {
                                // Front Side
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(ElectricBlue.copy(alpha = 0.2f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "QUESTION",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = ElectricBlue,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        text = currentCard.question,
                                        style = MaterialTheme.typography.displayMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText,
                                        fontSize = 20.sp
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Flip, contentDescription = null, tint = SecondaryText, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Tap card to reveal answer",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = SecondaryText,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            } else {
                                // Back Side
                                Column(
                                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(CyberPurple.copy(alpha = 0.3f))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "EXPLANATION & ANSWER",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = CyberPurple,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        text = currentCard.answer,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = PrimaryText,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Spaced Repetition Rating Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        if (currentCard != null) viewModel.setFlashcardStatus(currentCard, "NEEDS_REVIEW")
                        if (currentIndex < flashcardsList.size - 1) {
                            currentIndex++
                            isFlipped = false
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("flashcard_again_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusDanger.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusDanger)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Again", tint = StatusDanger, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Needs Review", color = StatusDanger, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        if (currentCard != null) viewModel.setFlashcardStatus(currentCard, "MASTERED")
                        viewModel.addXpPoints(15)
                        if (currentIndex < flashcardsList.size - 1) {
                            currentIndex++
                            isFlipped = false
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("flashcard_mastered_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusSuccess.copy(alpha = 0.25f)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StatusSuccess)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Mastered", tint = StatusSuccess, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mastered (+15 XP)", color = StatusSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
