package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.QuizQuestionEntity
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardBorderDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel
import org.json.JSONArray

@Composable
fun QuizScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val allQuestions by viewModel.quizQuestions.collectAsState()
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableStateOf(0) }
    var isSubmitted by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }

    val currentQuestion = if (allQuestions.isNotEmpty()) allQuestions.getOrNull(currentIndex) else null

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (isCompleted || currentQuestion == null) {
            // Quiz Result Screen
            QuizResultView(
                score = score,
                total = allQuestions.size,
                onRestart = {
                    currentIndex = 0
                    score = 0
                    selectedOption = null
                    isSubmitted = false
                    isCompleted = false
                },
                onGoHome = { viewModel.navigateTo("HOME") }
            )
        } else {
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
                        IconButton(
                            onClick = { viewModel.navigateTo("HOME") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CardDark)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                        }

                        Text(
                            text = "Interactive Quiz Engine",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )

                        // Difficulty Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ElectricBlue.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Question ${currentIndex + 1}/${allQuestions.size}",
                                style = MaterialTheme.typography.labelMedium,
                                color = ElectricBlueLight,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Question Card
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = CardDark,
                        borderColor = GlassBorder
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Type: ${currentQuestion.type}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ElectricBlueLight,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "Difficulty: ${currentQuestion.difficulty}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SecondaryText,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Parse Options
                    val options = remember(currentQuestion) {
                        try {
                            val jsonArr = JSONArray(currentQuestion.optionsJson)
                            (0 until jsonArr.length()).map { jsonArr.getString(it) }
                        } catch (e: Exception) {
                            listOf("Option A", "Option B", "Option C", "Option D")
                        }
                    }

                    // Options List
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        options.forEach { option ->
                            val isSelected = selectedOption == option
                            val isCorrect = option == currentQuestion.correctAnswer

                            val optionBg = when {
                                isSubmitted && isCorrect -> StatusSuccess.copy(alpha = 0.25f)
                                isSubmitted && isSelected && !isCorrect -> StatusDanger.copy(alpha = 0.25f)
                                isSelected -> ElectricBlue.copy(alpha = 0.3f)
                                else -> CardDark
                            }

                            val optionBorder = when {
                                isSubmitted && isCorrect -> StatusSuccess
                                isSubmitted && isSelected && !isCorrect -> StatusDanger
                                isSelected -> ElectricBlue
                                else -> GlassBorder
                            }

                            GlassCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("quiz_option_$option"),
                                backgroundColor = optionBg,
                                borderColor = optionBorder,
                                onClick = {
                                    if (!isSubmitted) selectedOption = option
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = PrimaryText,
                                        fontSize = 14.sp
                                    )

                                    if (isSubmitted && isCorrect) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = StatusSuccess)
                                    } else if (isSubmitted && isSelected && !isCorrect) {
                                        Icon(Icons.Default.Cancel, contentDescription = "Wrong", tint = StatusDanger)
                                    }
                                }
                            }
                        }
                    }

                    // Explanation Box if submitted
                    if (isSubmitted) {
                        Spacer(modifier = Modifier.height(16.dp))
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = SurfaceDark,
                            borderColor = GlassBorder
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "💡 Explanation:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ElectricBlueLight,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentQuestion.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryText,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // Action Button
                Button(
                    onClick = {
                        if (!isSubmitted) {
                            if (selectedOption != null) {
                                if (selectedOption == currentQuestion.correctAnswer) {
                                    score++
                                    viewModel.addXpPoints(25)
                                }
                                isSubmitted = true
                            }
                        } else {
                            if (currentIndex < allQuestions.size - 1) {
                                currentIndex++
                                selectedOption = null
                                isSubmitted = false
                            } else {
                                isCompleted = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(bottom = 10.dp)
                        .testTag("quiz_submit_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isSubmitted) ElectricBlue else StatusSuccess
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (!isSubmitted) "Submit Answer" else if (currentIndex < allQuestions.size - 1) "Next Question ➡️" else "View Score & Leaderboard",
                        color = PrimaryText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuizResultView(
    score: Int,
    total: Int,
    onRestart: () -> Unit,
    onGoHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.EmojiEvents,
            contentDescription = "Trophy",
            tint = ElectricBlue,
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Quiz Completed!",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = PrimaryText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You scored $score out of $total",
            style = MaterialTheme.typography.titleLarge,
            color = ElectricBlueLight,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardDark,
            borderColor = GlassBorder
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏆 XP Earned: +${score * 25} XP",
                    style = MaterialTheme.typography.titleMedium,
                    color = StatusSuccess,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Accuracy: ${(score * 100) / maxOf(total, 1)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onRestart,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.RestartAlt, contentDescription = "Retry", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Retry Quiz", color = PrimaryText)
            }

            Button(
                onClick = onGoHome,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Home", color = PrimaryText, fontWeight = FontWeight.Bold)
            }
        }
    }
}
