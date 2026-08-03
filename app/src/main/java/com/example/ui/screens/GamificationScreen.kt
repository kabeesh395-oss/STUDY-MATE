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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BenAvatar
import com.example.ui.components.BenMood
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardBorderDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MutedText
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

data class MissionItem(
    val id: String,
    val title: String,
    val rewardText: String,
    val xpReward: Int,
    val coinsReward: Int,
    var isCompleted: Boolean = false
)

@Composable
fun GamificationScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val profile = viewModel.userProfile.collectAsState().value

    var missions by remember {
        mutableStateOf(
            listOf(
                MissionItem("m1", "Read 2 Chapters in Study Notes", "+100 XP • 20 Coins", 100, 20, false),
                MissionItem("m2", "Solve 25 MCQs in Exam Mode", "+150 XP • 40 Coins", 150, 40, false),
                MissionItem("m3", "Upload & Summarize Study PDF", "+120 XP • 30 Coins", 120, 30, false),
                MissionItem("m4", "Complete Daily Flashcard Revision", "+80 XP • 15 Coins", 80, 15, false),
                MissionItem("m5", "Finish 1 Full Unit Mock Test", "+200 XP • 50 Coins", 200, 50, false)
            )
        )
    }

    val streakMilestones = listOf(
        "7 Days" to "Special Badge 🎖️",
        "15 Days" to "Premium Avatar 🤖",
        "30 Days" to "Legend Badge 👑",
        "100 Days" to "Hall Of Fame 🏆"
    )

    val streak = profile?.streakDays ?: 0
    val xp = profile?.xpPoints ?: 0

    val badges = listOf(
        BadgeItem("b1", "Streak Master", "Maintain 7+ days continuous study streak", "🔥", streak >= 7),
        BadgeItem("b2", "Flashcard Scholar", "Master flashcards across subjects", "⚡", xp >= 300),
        BadgeItem("b3", "Quiz Champion", "Score 100% accuracy in quizzes", "🏆", xp >= 500),
        BadgeItem("b4", "Night Owl Engineer", "Study after 11 PM for consecutive days", "🦉", xp >= 800),
        BadgeItem("b5", "Ben's Legend", "Achieve Level 10 Learner Status", "👑", xp >= 1000)
    )

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
            // Header
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
                            text = "Ben • Gamification & XP",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Text(
                            text = "Earn XP, Complete Missions & Rank Up",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.size(48.dp))
                }
            }

            // User Level & Ben Assistant Header Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BenAvatar(mood = BenMood.CELEBRATING, size = 84.dp)

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Level 10 • Learner",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = NeonGreen
                        )

                        Text(
                            text = "Next Tier: Level 20 Expert",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryText
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // XP Progress Bar
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("XP Progress", style = MaterialTheme.typography.labelSmall, color = MutedText)
                                Text("${profile?.xpPoints ?: 1450} / 2000 XP", style = MaterialTheme.typography.labelSmall, color = NeonCyan, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { ((profile?.xpPoints ?: 1450) % 1000) / 1000f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = NeonGreen,
                                trackColor = SurfaceDark
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            RewardStat("XP Points", "${profile?.xpPoints ?: 1450} XP", Icons.Default.EmojiEvents)
                            RewardStat("Coins", "${profile?.coins ?: 320} 🪙", Icons.Default.MonetizationOn)
                            RewardStat("Streak", "12 Days 🔥", Icons.Default.AutoAwesome)
                        }
                    }
                }
            }

            // Study Streak Milestones
            item {
                Column {
                    Text(
                        text = "🔥 Streak Milestones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        streakMilestones.forEach { (days, reward) ->
                            GlassCard(
                                modifier = Modifier.weight(1f),
                                backgroundColor = SurfaceDark,
                                borderColor = FlameOrange.copy(alpha = 0.3f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(days, fontWeight = FontWeight.ExtraBold, color = FlameOrange, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(reward, fontSize = 10.sp, color = SecondaryText)
                                }
                            }
                        }
                    }
                }
            }

            // Daily & Weekly Missions
            item {
                Text(
                    text = "🎯 Daily & Weekly Missions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )
            }

            items(missions) { mission ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = if (mission.isCompleted) SurfaceDark else CardDark,
                    borderColor = if (mission.isCompleted) StatusSuccess.copy(alpha = 0.4f) else GlassBorder
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = mission.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )
                            Text(
                                text = mission.rewardText,
                                style = MaterialTheme.typography.bodySmall,
                                color = NeonGreen,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = {
                                if (!mission.isCompleted) {
                                    missions = missions.map {
                                        if (it.id == mission.id) it.copy(isCompleted = true) else it
                                    }
                                    viewModel.addXpAndCoins(mission.xpReward, mission.coinsReward)
                                }
                            },
                            enabled = !mission.isCompleted,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (mission.isCompleted) SurfaceDark else NeonGreen,
                                disabledContainerColor = SurfaceDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (mission.isCompleted) "Completed ✓" else "Claim",
                                color = if (mission.isCompleted) SecondaryText else Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Unlocked Badges
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "🏆 Unlocked Badges",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )
            }

            items(badges) { badge ->
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = if (badge.isUnlocked) CardDark else SurfaceDark,
                    borderColor = if (badge.isUnlocked) ElectricBlue.copy(alpha = 0.5f) else CardBorderDark
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = badge.emoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = badge.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (badge.isUnlocked) PrimaryText else SecondaryText,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = badge.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SecondaryText,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (badge.isUnlocked) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Unlocked", tint = StatusSuccess)
                        } else {
                            Icon(Icons.Default.Lock, contentDescription = "Locked", tint = SecondaryText)
                        }
                    }
                }
            }
        }
    }
}

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val isUnlocked: Boolean
)

@Composable
fun RewardStat(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, tint = ElectricBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
        Text(text = label, style = MaterialTheme.typography.labelMedium, fontSize = 10.sp, color = SecondaryText)
    }
}
