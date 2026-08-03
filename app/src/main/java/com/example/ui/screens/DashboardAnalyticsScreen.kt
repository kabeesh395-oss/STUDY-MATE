package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedBarChart
import com.example.ui.components.GlassCard
import com.example.ui.components.ProgressRing
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun DashboardAnalyticsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState(initial = null)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo("HOME") },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CardDark)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Analytics & Performance",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Text(
                            text = "Study Hours, Quiz Accuracy & Mastery Insights",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            // Stat Cards Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val studyHrs = if ((userProfile?.streakDays ?: 0) > 0) "${String.format("%.1f", (userProfile?.streakDays ?: 0) * 1.5f)} hrs" else "0.0 hrs"
                    AnalyticsStatPill("Total Study", studyHrs, if ((userProfile?.streakDays ?: 0) > 0) "+18%" else "0%", Icons.Default.Schedule, Modifier.weight(1f))
                    AnalyticsStatPill("Quiz Accuracy", "88%", "+5%", Icons.Default.CheckCircle, Modifier.weight(1f))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AnalyticsStatPill("Mastered Cards", "142", "+24", Icons.Default.Psychology, Modifier.weight(1f))
                    AnalyticsStatPill("Streak Record", "12 Days", "Best 14", Icons.Default.EmojiEvents, Modifier.weight(1f))
                }
            }

            // Chart Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Daily Focus Distribution (Hours)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        AnimatedBarChart(
                            data = listOf(
                                "Mon" to 3.5f / 5f,
                                "Tue" to 4.2f / 5f,
                                "Wed" to 2.8f / 5f,
                                "Thu" to 4.9f / 5f,
                                "Fri" to 3.9f / 5f,
                                "Sat" to 5.0f / 5f,
                                "Sun" to 3.2f / 5f
                            )
                        )
                    }
                }
            }

            // Subject Completion Breakdown
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Syllabus Completion Breakdown",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        SubjectProgressRow("Artificial Intelligence", 75)
                        SubjectProgressRow("Software Engineering", 60)
                        SubjectProgressRow("Python Programming", 90)
                        SubjectProgressRow("Data Structures", 82)
                    }
                }
            }
        }
    }
}

@Composable
fun AnalyticsStatPill(
    label: String,
    value: String,
    change: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        backgroundColor = CardDark,
        borderColor = GlassBorder
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = label, tint = ElectricBlue, modifier = Modifier.size(20.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusSuccess.copy(alpha = 0.2f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = change, style = MaterialTheme.typography.labelMedium, color = StatusSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = value, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = PrimaryText, fontSize = 20.sp)
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = SecondaryText, fontSize = 11.sp)
        }
    }
}

@Composable
fun SubjectProgressRow(subject: String, percent: Int) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = subject, style = MaterialTheme.typography.bodyMedium, color = PrimaryText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(text = "$percent%", style = MaterialTheme.typography.bodyMedium, color = ElectricBlueLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape)
                .background(SurfaceDark)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(fraction = percent / 100f)
                    .clip(CircleShape)
                    .background(ElectricBlue)
            )
        }
    }
}
