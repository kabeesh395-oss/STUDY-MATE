package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardBorderDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueDark
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MutedText
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun CareerHubScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    var selectedSectionTab by remember { mutableStateOf("OVERVIEW") } // OVERVIEW, LINKEDIN, GITHUB, LEETCODE, UNSTOP, INSTAGRAM, PLATFORMS, AI_ASSISTANT

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 20.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.navigateTo("HOME") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceDark)
                                .border(1.dp, GlassBorder, CircleShape)
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Career & Professional Hub",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = PrimaryText,
                                fontSize = 22.sp
                            )
                            Text(
                                text = "Placements, Internships & Developer Portfolio",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedText,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(ElectricBlue.copy(alpha = 0.15f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Work, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PRO HUB", style = MaterialTheme.typography.labelMedium, color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }
            }

            // Navigation Chips Bar
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val tabs = listOf(
                        "OVERVIEW" to "Overview",
                        "CERTIFICATES" to "Certificates",
                        "LINKEDIN" to "LinkedIn",
                        "GITHUB" to "GitHub",
                        "LEETCODE" to "LeetCode",
                        "UNSTOP" to "Unstop",
                        "INSTAGRAM" to "Instagram",
                        "PLATFORMS" to "Official Platforms & Links",
                        "AI_ASSISTANT" to "AI Assistant"
                    )

                    items(tabs) { (key, label) ->
                        val isSel = selectedSectionTab == key
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSel) ElectricBlue else SurfaceDark)
                                .border(1.dp, if (isSel) ElectricBlue else GlassBorder, RoundedCornerShape(16.dp))
                                .clickable { selectedSectionTab = key }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("career_tab_$key")
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isSel) PrimaryText else SecondaryText,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Tab Content rendering based on selectedSectionTab
            when (selectedSectionTab) {
                "OVERVIEW" -> {
                    // 1. Career Metric Dashboard
                    item { DashboardSection() }

                    // 2. Quick Access Cards Grid
                    item { QuickAccessGrid(onOpenUrl = { uriHandler.openUri(it) }) }

                    // 3. AI Career Assistant Recommendation Card
                    item { AiCareerAssistantCard(viewModel = viewModel, onOpenAiScreen = { selectedSectionTab = "AI_ASSISTANT" }) }

                    // 4. Notifications & Alerts
                    item { CareerNotificationsSection() }
                }

                "CERTIFICATES" -> {
                    item { CertificatesSection(onOpenUrl = { uriHandler.openUri(it) }) }
                }

                "LINKEDIN" -> {
                    item { LinkedInSection(onOpenUrl = { uriHandler.openUri("https://www.linkedin.com") }, viewModel = viewModel) }
                }

                "GITHUB" -> {
                    item { GitHubSection(onOpenUrl = { uriHandler.openUri("https://github.com") }, viewModel = viewModel) }
                }

                "LEETCODE" -> {
                    item { LeetCodeSection(onOpenUrl = { uriHandler.openUri("https://leetcode.com") }, viewModel = viewModel) }
                }

                "UNSTOP" -> {
                    item { UnstopSection(onOpenUrl = { uriHandler.openUri("https://unstop.com") }) }
                }

                "INSTAGRAM" -> {
                    item { InstagramSection(onOpenUrl = { uriHandler.openUri("https://www.instagram.com") }) }
                }

                "PLATFORMS" -> {
                    item { OtherCodingPlatformsSection(onOpenUrl = { uriHandler.openUri(it) }) }
                }

                "AI_ASSISTANT" -> {
                    item { AiCareerAssistantDetailed(viewModel = viewModel) }
                }
            }
        }
    }
}

/* ========================================================================= */
/* 8. DASHBOARD SECTION METRICS                                              */
/* ========================================================================= */
@Composable
fun DashboardSection() {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            text = "Professional Dashboard",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("LinkedIn Score", "88/100", "+5 pts", Icons.Default.Work, Color(0xFF0A66C2), Modifier.weight(1f))
            MetricCard("GitHub Commits", "245", "+12 this wk", Icons.Default.Code, Color(0xFF2DBA4E), Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("LeetCode Solved", "128", "60E • 52M • 16H", Icons.Default.Build, Color(0xFFFFA116), Modifier.weight(1f))
            MetricCard("Unstop Apps", "6 Active", "2 Shortlisted", Icons.Default.School, Color(0xFF1B365D), Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("Coding Streak", "14 Days 🔥", "Best 21", Icons.Default.EmojiEvents, FlameOrange, Modifier.weight(1f))
            MetricCard("Weekly Learning", "16.5 Hrs", "Top 5%", Icons.Default.BarChart, CyberPurple, Modifier.weight(1f))
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        cornerRadius = 24.dp,
        backgroundColor = SurfaceDark,
        borderColor = GlassBorder
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(18.dp))
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0x0DFFFFFF))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = subtitle, style = MaterialTheme.typography.labelMedium, fontSize = 9.sp, color = SecondaryText, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = PrimaryText, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MutedText, fontSize = 11.sp)
        }
    }
}

/* ========================================================================= */
/* 10. QUICK ACCESS CARDS WITH OPEN BUTTONS                                  */
/* ========================================================================= */
@Composable
fun QuickAccessGrid(onOpenUrl: (String) -> Unit) {
    val platforms = listOf(
        PlatformItem("LinkedIn", "https://www.linkedin.com", "Networking & Jobs", Color(0xFF0A66C2), "💼"),
        PlatformItem("GitHub", "https://github.com", "Code & Portfolio", Color(0xFF24292E), "🐙"),
        PlatformItem("LeetCode", "https://leetcode.com", "DSA & Problem Solving", Color(0xFFFFA116), "⚡"),
        PlatformItem("Unstop", "https://unstop.com", "Hackathons & Internships", Color(0xFF1B365D), "🚀"),
        PlatformItem("Instagram", "https://www.instagram.com", "Coding Reels & Tech Tips", Color(0xFFE4405F), "📸"),
        PlatformItem("HackerRank", "https://www.hackerrank.com", "Coding Skill Certificates", Color(0xFF2EC4B6), "🎯"),
        PlatformItem("CodeChef", "https://www.codechef.com", "Competitive Programming", Color(0xFF5B4636), "👨‍🍳"),
        PlatformItem("GeeksforGeeks", "https://www.geeksforgeeks.org", "CS Articles & Practice", Color(0xFF2F9E44), "📗"),
        PlatformItem("Kaggle", "https://www.kaggle.com", "AI & Data Science Hub", Color(0xFF20BEFF), "📊"),
        PlatformItem("Codeforces", "https://codeforces.com", "Global CP Contests", Color(0xFF1F8ACB), "🏆")
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Quick Access Platforms",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryText
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(platforms) { platform ->
                GlassCard(
                    modifier = Modifier
                        .width(150.dp)
                        .clickable { onOpenUrl(platform.url) },
                    cornerRadius = 24.dp,
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = platform.emoji, fontSize = 22.sp)
                            Icon(Icons.Default.Launch, contentDescription = "Open", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = platform.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
                        Text(text = platform.category, style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 10.sp, maxLines = 1)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { onOpenUrl(platform.url) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = platform.color),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Open", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

data class PlatformItem(
    val name: String,
    val url: String,
    val category: String,
    val color: Color,
    val emoji: String
)

/* ========================================================================= */
/* 1. LINKEDIN SECTION                                                        */
/* ========================================================================= */
@Composable
fun LinkedInSection(onOpenUrl: () -> Unit, viewModel: StudyViewModel) {
    var aiFeedbackText by remember { mutableStateOf("") }
    var isAnalyzing by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // LinkedIn Banner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFF0A66C2).copy(alpha = 0.3f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0A66C2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("in", color = Color.White, fontWeight = FontWeight.Black, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("LinkedIn Career Hub", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text("Networking, Personal Branding & Jobs", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onOpenUrl,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A66C2)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Open LinkedIn", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // LinkedIn Guide Cards
        Text("Profile & Branding Guides", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val guides = listOf(
            "Create LinkedIn Profile Guide" to "Step-by-step setup for engineering students: headline, summary, skills & certifications.",
            "Resume Optimization Tips" to "How to align your LinkedIn experiences with ATS-friendly keywords for top tech companies.",
            "Personal Branding Guide" to "Post your project demos, write technical articles, and build high engineering credibility.",
            "Connection Suggestions" to "Network with university alumni, HR recruiters, and senior software engineering mentors."
        )

        guides.forEach { (title, desc) ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                    }
                }
            }
        }

        // AI Profile Reviewer Card
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            backgroundColor = SurfaceDark,
            borderColor = ElectricBlue.copy(alpha = 0.3f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI LinkedIn Profile Review", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (aiFeedbackText.isEmpty()) "Get instant AI feedback on your LinkedIn summary, headline, and project showcase." else aiFeedbackText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        isAnalyzing = true
                        viewModel.sendChatMessage(
                            userPrompt = "Review my student LinkedIn profile. Give me 3 high-impact headline suggestions and 3 tips for my profile summary as a Computer Science student.",
                            modeOverride = "TEACHER"
                        )
                        aiFeedbackText = "AI Review Generated! Check 'AI Tutor' tab for detailed personalized headline and summary recommendations."
                        isAnalyzing = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(if (isAnalyzing) "Analyzing..." else "Run AI Profile Review", color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

/* ========================================================================= */
/* 2. GITHUB SECTION                                                         */
/* ========================================================================= */
@Composable
fun GitHubSection(onOpenUrl: () -> Unit, viewModel: StudyViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = GlassBorder
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🐙", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("GitHub Portfolio Hub", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text("Repositories, Commit Streak & Projects", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onOpenUrl,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24292E)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Open GitHub", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // Commit Streak Tracker & Portfolio
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            backgroundColor = CardDark,
            borderColor = GlassBorder
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Commit Streak Tracker", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Current Streak: 14 Days (245 commits in 2026)", style = MaterialTheme.typography.bodyMedium, color = StatusSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(12.dp))

                // Simulated Contribution Grid
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    repeat(12) { col ->
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            repeat(5) { row ->
                                val active = (col + row) % 3 != 0
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(if (active) Color(0xFF2DBA4E) else Color(0x1AFFFFFF))
                                )
                            }
                        }
                    }
                }
            }
        }

        // AI Project Suggestions
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            backgroundColor = SurfaceDark,
            borderColor = CyberPurple.copy(alpha = 0.3f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Portfolio Project Ideas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                }

                Spacer(modifier = Modifier.height(10.dp))

                val ideas = listOf(
                    "1. Smart Revision Planner App (Kotlin + Compose + Room)",
                    "2. AI Code Reviewer Bot (Python + Gemini API)",
                    "3. Distributed Key-Value Store in Go / C++"
                )

                ideas.forEach { idea ->
                    Text(text = idea, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        viewModel.sendChatMessage("Generate 3 unique resume-worthy full-stack engineering project ideas with system architecture for my GitHub portfolio.", modeOverride = "TEACHER")
                        viewModel.navigateTo("AI_CHAT")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Ask AI for Detailed Architecture", color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

/* ========================================================================= */
/* 3. LEETCODE SECTION                                                        */
/* ========================================================================= */
@Composable
fun LeetCodeSection(onOpenUrl: () -> Unit, viewModel: StudyViewModel) {
    var selectedDifficultyFilter by remember { mutableStateOf("ALL") }

    val problems = listOf(
        ProblemItem("1", "Two Sum", "EASY", "Array, Hash Table", true),
        ProblemItem("20", "Valid Parentheses", "EASY", "Stack, String", true),
        ProblemItem("200", "Number of Islands", "MEDIUM", "BFS, DFS, Graph", false),
        ProblemItem("146", "LRU Cache", "MEDIUM", "Hash Table, Linked List", true),
        ProblemItem("42", "Trapping Rain Water", "HARD", "Two Pointers, Stack", false)
    )

    val filtered = when (selectedDifficultyFilter) {
        "EASY" -> problems.filter { it.difficulty == "EASY" }
        "MEDIUM" -> problems.filter { it.difficulty == "MEDIUM" }
        "HARD" -> problems.filter { it.difficulty == "HARD" }
        else -> problems
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFFFFA116).copy(alpha = 0.3f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("⚡", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("LeetCode Arena", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text("128 Solved • Easy 60 | Med 52 | Hard 16", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onOpenUrl,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA116)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Open LeetCode", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Launch, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // Difficulty Filters & Daily Challenge
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            backgroundColor = CardDark,
            borderColor = GlassBorder
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Daily Problem Tracker", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val filters = listOf("ALL", "EASY", "MEDIUM", "HARD")
                    filters.forEach { filter ->
                        val isSel = selectedDifficultyFilter == filter
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) ElectricBlue else SurfaceDark)
                                .clickable { selectedDifficultyFilter = filter }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isSel) PrimaryText else SecondaryText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                filtered.forEach { prob ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (prob.isSolved) Icons.Default.CheckCircle else Icons.Default.Build,
                                contentDescription = null,
                                tint = if (prob.isSolved) StatusSuccess else SecondaryText,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("${prob.id}. ${prob.title}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 13.sp)
                                Text(prob.tags, style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 10.sp)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (prob.difficulty) {
                                        "EASY" -> StatusSuccess.copy(alpha = 0.2f)
                                        "MEDIUM" -> StatusWarning.copy(alpha = 0.2f)
                                        else -> StatusDanger.copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = prob.difficulty,
                                style = MaterialTheme.typography.labelMedium,
                                color = when (prob.difficulty) {
                                    "EASY" -> StatusSuccess
                                    "MEDIUM" -> StatusWarning
                                    else -> StatusDanger
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ProblemItem(
    val id: String,
    val title: String,
    val difficulty: String,
    val tags: String,
    val isSolved: Boolean
)

/* ========================================================================= */
/* 4. UNSTOP SECTION                                                          */
/* ========================================================================= */
@Composable
fun UnstopSection(onOpenUrl: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFF1B365D).copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🚀", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Unstop Opportunities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text("Hackathons, Competitions & Internships", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onOpenUrl,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B365D)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Open Unstop", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        Text("Live Hackathons & Opportunities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val unstopList = listOf(
            UnstopEvent("Flipkart GRID 6.0 Software Challenge", "Hackathon • $10,000 Prize Pool", "Ends in 4 Days", "REGISTERED"),
            UnstopEvent("Google Summer of Code 2026 Prep", "Open Source Mentorship", "Opens Next Month", "APPLY"),
            UnstopEvent("Amazon ML Summer School", "Machine Learning & AI Workshop", "3 Days Left", "APPLY"),
            UnstopEvent("Tata Crucible Campus Quiz 2026", "National Tech & Business Quiz", "Live Now", "APPLY")
        )

        unstopList.forEach { evt ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(evt.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("${evt.type} • ${evt.deadline}", style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                    }

                    Button(
                        onClick = onOpenUrl,
                        colors = ButtonDefaults.buttonColors(containerColor = if (evt.status == "REGISTERED") StatusSuccess else ElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text(evt.status, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

data class UnstopEvent(val title: String, val type: String, val deadline: String, val status: String)

/* ========================================================================= */
/* 5. INSTAGRAM SECTION                                                       */
/* ========================================================================= */
@Composable
fun InstagramSection(onOpenUrl: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFFE4405F).copy(alpha = 0.3f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📸", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Educational Instagram", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text("Tech Creators, Reels & Productivity Tips", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = onOpenUrl,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE4405F)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Open Instagram", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        Text("Recommended Creator Channels & Topics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val instaChannels = listOf(
            "Coding & DSA Short Reels" to "60-second visual explanations of graph algorithms, time complexity & system design.",
            "AI & Machine Learning Updates" to "Daily reels covering Gemini, LLM prompt engineering, and paper summaries.",
            "Student Productivity & Exam Hacks" to "Pomodoro techniques, spaced repetition revision tips, and engineering study schedules."
        )

        instaChannels.forEach { (title, desc) ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SmartDisplay, contentDescription = null, tint = CyberPink, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(desc, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

/* ========================================================================= */
/* 6. OTHER CODING PLATFORMS SECTION                                         */
/* ========================================================================= */
@Composable
fun OtherCodingPlatformsSection(onOpenUrl: (String) -> Unit) {
    val platforms = listOf(
        PlatformItem("HackerRank", "https://www.hackerrank.com", "Skill Certifications & Python/SQL Tests", Color(0xFF2EC4B6), "🎯"),
        PlatformItem("CodeChef", "https://www.codechef.com", "Long Challenges & Star Ratings", Color(0xFF5B4636), "👨‍🍳"),
        PlatformItem("GeeksforGeeks", "https://www.geeksforgeeks.org", "Data Structures, Algorithms & CS Core", Color(0xFF2F9E44), "📗"),
        PlatformItem("Kaggle", "https://www.kaggle.com", "Jupyter Notebooks, Datasets & ML Contests", Color(0xFF20BEFF), "📊"),
        PlatformItem("Microsoft Learn", "https://learn.microsoft.com", "Free Azure, AI & Developer Training", Color(0xFF00A4EF), "💻"),
        PlatformItem("Google Cloud Skills Boost", "https://www.cloudskillsboost.google", "Free Google Cloud & Android Labs", Color(0xFF4285F4), "☁️"),
        PlatformItem("freeCodeCamp", "https://www.freecodecamp.org", "Free Web Dev & Python Certifications", Color(0xFF0A0A23), "🔥"),
        PlatformItem("Coursera (Free Courses)", "https://www.coursera.org", "University Courses & Financial Aid", Color(0xFF0056D2), "🎓"),
        PlatformItem("edX", "https://www.edx.org", "MIT, Harvard & Global University Courses", Color(0xFF0B2C4D), "🏛️"),
        PlatformItem("NPTEL", "https://nptel.ac.in", "IIT Certified Online Core Engineering", Color(0xFF8B0000), "📜"),
        PlatformItem("Cisco Skills for All", "https://skillsforall.com", "Free Cisco Networking & Cybersecurity", Color(0xFF1BA0D7), "🛡️"),
        PlatformItem("Codeforces", "https://codeforces.com", "Div 1, Div 2, Div 3 Global Rating Contests", Color(0xFF1F8ACB), "🏆")
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Official Coding & Practice Platforms", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        platforms.forEach { plat ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenUrl(plat.url) },
                cornerRadius = 22.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(plat.emoji, fontSize = 26.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(plat.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 15.sp)
                            Text(plat.category, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = { onOpenUrl(plat.url) },
                        colors = ButtonDefaults.buttonColors(containerColor = plat.color),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        Text("Open", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

/* ========================================================================= */
/* CERTIFICATES SECTION                                                     */
/* ========================================================================= */
@Composable
fun CertificatesSection(onOpenUrl: (String) -> Unit) {
    val providers = listOf(
        CertProvider("Google", "https://www.cloudskillsboost.google", "Google Cloud Skills Boost & Android", Color(0xFF4285F4), "🌐"),
        CertProvider("Microsoft", "https://learn.microsoft.com", "Azure Fundamentals & C# / .NET", Color(0xFF00A4EF), "💻"),
        CertProvider("IBM", "https://www.ibm.com/training", "IBM SkillsBuild & Data Science", Color(0xFF052147), "🤖"),
        CertProvider("Cisco", "https://skillsforall.com", "Cisco Networking & Cybersecurity", Color(0xFF1BA0D7), "🛡️"),
        CertProvider("Oracle", "https://education.oracle.com", "Oracle Cloud Infrastructure & Java", Color(0xFFF80000), "☕"),
        CertProvider("AWS", "https://aws.amazon.com/education/aws-educate", "AWS Educate & Cloud Practitioner", Color(0xFFFF9900), "☁️"),
        CertProvider("freeCodeCamp", "https://www.freecodecamp.org", "300hr Full-Stack & Algorithm Certs", Color(0xFF0A0A23), "🔥"),
        CertProvider("NPTEL", "https://nptel.ac.in", "IIT Certified Core Engineering Courses", Color(0xFF8B0000), "🎓")
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = StatusSuccess.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📜", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Certifications Tracker & Hub", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                        Text("Track, verify and claim free industry certifications", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CertStatPill("Completed", "3 Certs", StatusSuccess)
                    CertStatPill("In Progress", "2 Active", ElectricBlue)
                    CertStatPill("Next Recommended", "AWS Cloud", CyberPurple)
                }
            }
        }

        // 1. Completed Certificates
        Text("Completed Certificates (Verified)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val completed = listOf(
            CertItem("Google Cloud Digital Leader", "Google Cloud Skills Boost", "Earned: June 2026", "https://www.cloudskillsboost.google", true, 100),
            CertItem("Responsive Web Design", "freeCodeCamp", "Earned: May 2026", "https://www.freecodecamp.org", true, 100),
            CertItem("Java Programming Fundamentals", "NPTEL / IIT Madras", "Earned: April 2026", "https://nptel.ac.in", true, 100)
        )

        completed.forEach { item ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = StatusSuccess.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 13.sp)
                            Text("${item.provider} • ${item.date}", style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { onOpenUrl(item.url) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Text("View Badge", fontSize = 10.sp, color = ElectricBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 2. In Progress Certificates
        Text("Certificates In Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val inProgress = listOf(
            CertItem("AWS Cloud Practitioner Prep", "AWS Educate", "75% Completed (3 Modules Left)", "https://aws.amazon.com/education/aws-educate", false, 75),
            CertItem("Cybersecurity Essentials", "Cisco Skills for All", "40% Completed (5 Modules Left)", "https://skillsforall.com", false, 40)
        )

        inProgress.forEach { item ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 13.sp)
                            Text(item.provider, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                        }
                        Text("${item.progress}%", style = MaterialTheme.typography.labelLarge, color = ElectricBlue, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { item.progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = ElectricBlue,
                        trackColor = SurfaceDark
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onOpenUrl(item.url) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                    ) {
                        Text("Continue Module", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
                    }
                }
            }
        }

        // 3. Recommended Next Certificate
        Text("Recommended Next Certificate", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 22.dp,
            backgroundColor = SurfaceDark,
            borderColor = CyberPurple.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("AI Career Recommendation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Microsoft Certified: Azure AI Fundamentals (AI-900)\nFree learning path on Microsoft Learn to validate machine learning and AI concepts.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { onOpenUrl("https://learn.microsoft.com") },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enroll Free on Microsoft Learn", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
                }
            }
        }

        // 4. Free Certification Providers Grid
        Text("Free Certification Providers", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        providers.forEach { prov ->
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenUrl(prov.url) },
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(prov.emoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(prov.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
                            Text(prov.desc, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 10.sp)
                        }
                    }

                    Button(
                        onClick = { onOpenUrl(prov.url) },
                        colors = ButtonDefaults.buttonColors(containerColor = prov.color),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text("Explore", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CertStatPill(label: String, value: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color, fontSize = 13.sp)
            Text(label, style = MaterialTheme.typography.labelMedium, color = SecondaryText, fontSize = 10.sp)
        }
    }
}

data class CertProvider(val name: String, val url: String, val desc: String, val color: Color, val emoji: String)
data class CertItem(val title: String, val provider: String, val date: String, val url: String, val isCompleted: Boolean, val progress: Int)

/* ========================================================================= */
/* 7. AI CAREER ASSISTANT                                                     */
/* ========================================================================= */
@Composable
fun AiCareerAssistantCard(viewModel: StudyViewModel, onOpenAiScreen: () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 28.dp,
        backgroundColor = SurfaceDark,
        borderColor = ElectricBlue.copy(alpha = 0.4f)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ElectricBlue.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("AI Career & Placement Tutor", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                    Text("Daily Roadmap, Skill Scores & Advice", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CareerRoadmapPill("Profile Completion", "88%", StatusSuccess)
                CareerRoadmapPill("Daily Recommended Platform", "LeetCode (Medium Dynamic Programming)", ElectricBlue)
                CareerRoadmapPill("Weekly Goal", "Solve 5 LeetCode & Update GitHub Readme", CyberPurple)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onOpenAiScreen,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Open Full AI Career Advisor", color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CareerRoadmapPill(title: String, detail: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x0DFFFFFF))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
            Text(detail, style = MaterialTheme.typography.bodyMedium, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}

@Composable
fun AiCareerAssistantDetailed(viewModel: StudyViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = ElectricBlue.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🧠", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("AI Career Assistant Engine", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                        Text("Personalized Software Engineer Career Plan", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Skill Improvement Suggestions:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Master Graph Algorithms (BFS/DFS) on LeetCode for product company interviews.\n• Add a Kotlin Jetpack Compose project to GitHub to show Android mobile mastery.\n• Optimize LinkedIn headline to include 'Computer Science Undergrad | Android & AI Enthusiast'.", style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 12.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.sendChatMessage(
                            userPrompt = "Generate my 4-week Software Engineering Placement Preparation Roadmap with daily 2-hour breakdown (DSA, System Design, Projects, Mock Interviews).",
                            modeOverride = "TEACHER"
                        )
                        viewModel.navigateTo("AI_CHAT")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Generate 4-Week Placement Plan in AI Chat", color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

/* ========================================================================= */
/* 9. NOTIFICATIONS & ALERTS SECTION                                         */
/* ========================================================================= */
@Composable
fun CareerNotificationsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Career Alerts & Notifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val alerts = listOf(
            NotificationItem("Daily Coding Reminder", "Don't forget today's LeetCode Daily Challenge to keep your 14-day streak!", "Today, 8:00 AM", FlameOrange, Icons.Default.Code),
            NotificationItem("New Hackathon Alert", "Flipkart GRID 6.0 registrations ending soon on Unstop.", "Today, 10:30 AM", ElectricBlue, Icons.Default.Event),
            NotificationItem("Internship Alert", "Amazon ML Summer School applications are now open.", "Yesterday", StatusSuccess, Icons.Default.Work),
            NotificationItem("Contest Reminder", "Codeforces Div 2 Round starts tomorrow at 8:05 PM.", "Yesterday", CyberPurple, Icons.Default.Notifications)
        )

        alerts.forEach { alert ->
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(alert.color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = alert.icon, contentDescription = null, tint = alert.color, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(alert.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 13.sp)
                            Text(alert.time, style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 10.sp)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(alert.body, style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

data class NotificationItem(
    val title: String,
    val body: String,
    val time: String,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
