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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.data.model.AuthUserModel
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
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun CareerHubScreen(
    viewModel: StudyViewModel,
    authViewModel: AuthViewModel? = null,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val currentUser by authViewModel?.currentUser?.collectAsState() ?: remember { mutableStateOf(null) }
    var selectedSectionTab by remember { mutableStateOf("OVERVIEW") }
    var showEditProfilesDialog by remember { mutableStateOf(false) }

    val openUrlSafe = { url: String ->
        if (url.isNotBlank()) {
            val formatted = if (!url.startsWith("http://") && !url.startsWith("https://")) "https://$url" else url
            try {
                uriHandler.openUri(formatted)
            } catch (e: Exception) {
                // Ignore invalid scheme errors
            }
        }
    }

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
                                text = currentUser?.name?.let { "Personalized Portfolio for $it" } ?: "Placements, Internships & Developer Portfolio",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedText,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Button(
                        onClick = { showEditProfilesDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = PrimaryText, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Profiles", style = MaterialTheme.typography.labelMedium, color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 11.sp)
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
                    item { DashboardSection(user = currentUser, onConnectAccount = { showEditProfilesDialog = true }) }

                    // 2. Quick Access Cards Grid
                    item { QuickAccessGrid(user = currentUser, onOpenUrl = openUrlSafe, onConnectAccount = { showEditProfilesDialog = true }) }

                    // 3. AI Career Assistant Recommendation Card
                    item { AiCareerAssistantCard(user = currentUser, viewModel = viewModel, onOpenAiScreen = { selectedSectionTab = "AI_ASSISTANT" }) }

                    // 4. Notifications & Alerts
                    item { CareerNotificationsSection() }
                }

                "CERTIFICATES" -> {
                    item { CertificatesSection(onOpenUrl = openUrlSafe) }
                }

                "LINKEDIN" -> {
                    item { LinkedInSection(user = currentUser, onOpenUrl = openUrlSafe, onConnectAccount = { showEditProfilesDialog = true }, viewModel = viewModel) }
                }

                "GITHUB" -> {
                    item { GitHubSection(user = currentUser, onOpenUrl = openUrlSafe, onConnectAccount = { showEditProfilesDialog = true }, authViewModel = authViewModel, viewModel = viewModel) }
                }

                "LEETCODE" -> {
                    item { LeetCodeSection(user = currentUser, onOpenUrl = openUrlSafe, onConnectAccount = { showEditProfilesDialog = true }, viewModel = viewModel) }
                }

                "UNSTOP" -> {
                    item { UnstopSection(user = currentUser, onOpenUrl = openUrlSafe, onConnectAccount = { showEditProfilesDialog = true }) }
                }

                "INSTAGRAM" -> {
                    item { InstagramSection(onOpenUrl = openUrlSafe) }
                }

                "PLATFORMS" -> {
                    item { OtherCodingPlatformsSection(user = currentUser, onOpenUrl = openUrlSafe, onConnectAccount = { showEditProfilesDialog = true }) }
                }

                "AI_ASSISTANT" -> {
                    item { AiCareerAssistantDetailed(user = currentUser, viewModel = viewModel) }
                }
            }
        }

        // Edit Profiles Modal Dialog
        if (showEditProfilesDialog) {
            EditProfilesDialog(
                user = currentUser ?: AuthUserModel(),
                authViewModel = authViewModel,
                onDismiss = { showEditProfilesDialog = false }
            )
        }
    }
}

/* ========================================================================= */
/* EDIT PROFILES DIALOG                                                      */
/* ========================================================================= */
@Composable
fun EditProfilesDialog(
    user: AuthUserModel,
    authViewModel: AuthViewModel?,
    onDismiss: () -> Unit
) {
    var linkedinUrl by remember { mutableStateOf(user.linkedinUrl) }
    var githubUsername by remember { mutableStateOf(user.githubUsername) }
    var leetcodeUsername by remember { mutableStateOf(user.leetcodeUsername) }
    var unstopUrl by remember { mutableStateOf(user.unstopUrl) }
    var hackerrankUsername by remember { mutableStateOf(user.hackerrankUsername) }
    var codechefUsername by remember { mutableStateOf(user.codechefUsername) }
    var codeforcesUsername by remember { mutableStateOf(user.codeforcesUsername) }
    var kaggleUsername by remember { mutableStateOf(user.kaggleUsername) }
    var portfolioUrl by remember { mutableStateOf(user.portfolioUrl) }
    var resumeUrl by remember { mutableStateOf(user.resumeUrl) }

    var githubCommitsText by remember { mutableStateOf(user.githubCommits.toString()) }
    var leetcodeSolvedText by remember { mutableStateOf(user.leetcodeSolved.toString()) }
    var codingStreakText by remember { mutableStateOf(user.codingStreakDays.toString()) }
    var weeklyHoursText by remember { mutableStateOf(user.weeklyLearningHours.toString()) }

    var isFetchingGitHub by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Build, contentDescription = null, tint = ElectricBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Career Profiles & Links", color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Enter your real profile links & usernames to personalize your Career Hub:", style = MaterialTheme.typography.bodySmall, color = SecondaryText)

                // LinkedIn
                OutlinedTextField(
                    value = linkedinUrl,
                    onValueChange = { linkedinUrl = it },
                    label = { Text("LinkedIn Profile URL") },
                    placeholder = { Text("https://linkedin.com/in/username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // GitHub
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = githubUsername,
                        onValueChange = { githubUsername = it },
                        label = { Text("GitHub Username") },
                        placeholder = { Text("e.g. octocat") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                    )

                    Button(
                        onClick = {
                            if (githubUsername.isNotBlank()) {
                                isFetchingGitHub = true
                                authViewModel?.fetchLiveGithubStats(githubUsername)
                                isFetchingGitHub = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(52.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = PrimaryText, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
                        }
                    }
                }

                // LeetCode
                OutlinedTextField(
                    value = leetcodeUsername,
                    onValueChange = { leetcodeUsername = it },
                    label = { Text("LeetCode Username") },
                    placeholder = { Text("e.g. leetcode_user") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // Unstop
                OutlinedTextField(
                    value = unstopUrl,
                    onValueChange = { unstopUrl = it },
                    label = { Text("Unstop Profile URL") },
                    placeholder = { Text("https://unstop.com/user/username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // HackerRank
                OutlinedTextField(
                    value = hackerrankUsername,
                    onValueChange = { hackerrankUsername = it },
                    label = { Text("HackerRank Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // CodeChef
                OutlinedTextField(
                    value = codechefUsername,
                    onValueChange = { codechefUsername = it },
                    label = { Text("CodeChef Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // Codeforces
                OutlinedTextField(
                    value = codeforcesUsername,
                    onValueChange = { codeforcesUsername = it },
                    label = { Text("Codeforces Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // Kaggle
                OutlinedTextField(
                    value = kaggleUsername,
                    onValueChange = { kaggleUsername = it },
                    label = { Text("Kaggle Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // Portfolio Website
                OutlinedTextField(
                    value = portfolioUrl,
                    onValueChange = { portfolioUrl = it },
                    label = { Text("Portfolio Website URL") },
                    placeholder = { Text("https://myportfolio.com") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                // Resume URL
                OutlinedTextField(
                    value = resumeUrl,
                    onValueChange = { resumeUrl = it },
                    label = { Text("Resume Link (Google Drive / PDF)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Manual Statistics Updates:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = PrimaryText)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = githubCommitsText,
                        onValueChange = { githubCommitsText = it.filter { char -> char.isDigit() } },
                        label = { Text("GitHub Commits") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                    )
                    OutlinedTextField(
                        value = leetcodeSolvedText,
                        onValueChange = { leetcodeSolvedText = it.filter { char -> char.isDigit() } },
                        label = { Text("LeetCode Solved") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = codingStreakText,
                        onValueChange = { codingStreakText = it.filter { char -> char.isDigit() } },
                        label = { Text("Coding Streak (Days)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                    )
                    OutlinedTextField(
                        value = weeklyHoursText,
                        onValueChange = { weeklyHoursText = it },
                        label = { Text("Weekly Hours") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, unfocusedBorderColor = GlassBorder)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val updated = user.copy(
                        linkedinUrl = linkedinUrl.trim(),
                        githubUsername = githubUsername.trim().removePrefix("https://github.com/").removePrefix("@"),
                        leetcodeUsername = leetcodeUsername.trim().removePrefix("https://leetcode.com/u/").removePrefix("@"),
                        unstopUrl = unstopUrl.trim(),
                        hackerrankUsername = hackerrankUsername.trim().removePrefix("https://www.hackerrank.com/"),
                        codechefUsername = codechefUsername.trim().removePrefix("https://www.codechef.com/users/"),
                        codeforcesUsername = codeforcesUsername.trim().removePrefix("https://codeforces.com/profile/"),
                        kaggleUsername = kaggleUsername.trim().removePrefix("https://www.kaggle.com/"),
                        portfolioUrl = portfolioUrl.trim(),
                        resumeUrl = resumeUrl.trim(),
                        githubCommits = githubCommitsText.toIntOrNull() ?: user.githubCommits,
                        leetcodeSolved = leetcodeSolvedText.toIntOrNull() ?: user.leetcodeSolved,
                        codingStreakDays = codingStreakText.toIntOrNull() ?: user.codingStreakDays,
                        weeklyLearningHours = weeklyHoursText.toFloatOrNull() ?: user.weeklyLearningHours,
                        linkedinScore = if (linkedinUrl.isNotBlank()) 85 else 0,
                        unstopAppsCount = if (unstopUrl.isNotBlank()) 1 else 0
                    )
                    authViewModel?.updateUserProfile(updated)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Save & Sync to Firestore", color = PrimaryText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = SecondaryText)
            }
        },
        containerColor = SurfaceDark,
        shape = RoundedCornerShape(20.dp)
    )
}

/* ========================================================================= */
/* DASHBOARD SECTION METRICS                                                 */
/* ========================================================================= */
@Composable
fun DashboardSection(user: AuthUserModel?, onConnectAccount: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Professional Dashboard",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Text(
                text = "Tap any metric to edit",
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
                fontSize = 11.sp,
                modifier = Modifier.clickable { onConnectAccount() }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val hasLinkedin = !user?.linkedinUrl.isNullOrBlank()
            MetricCard(
                title = "LinkedIn Profile",
                value = if (hasLinkedin) "${user?.linkedinScore ?: 85}/100" else "Not Connected",
                subtitle = if (hasLinkedin) "Connected" else "+ Connect",
                icon = Icons.Default.Work,
                accentColor = Color(0xFF0A66C2),
                modifier = Modifier.weight(1f).clickable { onConnectAccount() }
            )

            val hasGithub = !user?.githubUsername.isNullOrBlank()
            MetricCard(
                title = "GitHub Commits",
                value = if (hasGithub) "${user?.githubCommits ?: 0}" else "Not Connected",
                subtitle = if (hasGithub) "@${user?.githubUsername}" else "+ Connect",
                icon = Icons.Default.Code,
                accentColor = Color(0xFF2DBA4E),
                modifier = Modifier.weight(1f).clickable { onConnectAccount() }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val hasLeetcode = !user?.leetcodeUsername.isNullOrBlank()
            MetricCard(
                title = "LeetCode Solved",
                value = if (hasLeetcode) "${user?.leetcodeSolved ?: 0}" else "Not Connected",
                subtitle = if (hasLeetcode) "@${user?.leetcodeUsername}" else "+ Connect",
                icon = Icons.Default.Build,
                accentColor = Color(0xFFFFA116),
                modifier = Modifier.weight(1f).clickable { onConnectAccount() }
            )

            val hasUnstop = !user?.unstopUrl.isNullOrBlank()
            MetricCard(
                title = "Unstop Apps",
                value = if (hasUnstop) "${user?.unstopAppsCount ?: 1} Active" else "Not Connected",
                subtitle = if (hasUnstop) "Connected" else "+ Connect",
                icon = Icons.Default.School,
                accentColor = Color(0xFF1B365D),
                modifier = Modifier.weight(1f).clickable { onConnectAccount() }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                title = "Coding Streak",
                value = "${user?.codingStreakDays ?: 0} Days 🔥",
                subtitle = if ((user?.codingStreakDays ?: 0) > 0) "Active Streak" else "Start Today",
                icon = Icons.Default.EmojiEvents,
                accentColor = FlameOrange,
                modifier = Modifier.weight(1f).clickable { onConnectAccount() }
            )
            MetricCard(
                title = "Weekly Learning",
                value = "${user?.weeklyLearningHours ?: 0.0f} Hrs",
                subtitle = if ((user?.weeklyLearningHours ?: 0.0f) > 0) "Logged Time" else "No Activity",
                icon = Icons.Default.BarChart,
                accentColor = CyberPurple,
                modifier = Modifier.weight(1f).clickable { onConnectAccount() }
            )
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
                        .background(if (subtitle.contains("Connect")) ElectricBlue.copy(alpha = 0.2f) else Color(0x0DFFFFFF))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 9.sp,
                        color = if (subtitle.contains("Connect")) ElectricBlue else SecondaryText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = PrimaryText, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MutedText, fontSize = 11.sp)
        }
    }
}

/* ========================================================================= */
/* QUICK ACCESS CARDS WITH OPEN BUTTONS                                     */
/* ========================================================================= */
@Composable
fun QuickAccessGrid(
    user: AuthUserModel?,
    onOpenUrl: (String) -> Unit,
    onConnectAccount: () -> Unit
) {
    val platforms = listOf(
        PlatformItem("LinkedIn", user?.linkedinUrl?.ifBlank { null } ?: "https://www.linkedin.com", "Networking & Jobs", Color(0xFF0A66C2), "💼", !user?.linkedinUrl.isNullOrBlank()),
        PlatformItem("GitHub", if (!user?.githubUsername.isNullOrBlank()) "https://github.com/${user.githubUsername}" else "https://github.com", "Code & Portfolio", Color(0xFF24292E), "🐙", !user?.githubUsername.isNullOrBlank()),
        PlatformItem("LeetCode", if (!user?.leetcodeUsername.isNullOrBlank()) "https://leetcode.com/u/${user.leetcodeUsername}" else "https://leetcode.com", "DSA & Problem Solving", Color(0xFFFFA116), "⚡", !user?.leetcodeUsername.isNullOrBlank()),
        PlatformItem("Unstop", user?.unstopUrl?.ifBlank { null } ?: "https://unstop.com", "Hackathons & Internships", Color(0xFF1B365D), "🚀", !user?.unstopUrl.isNullOrBlank()),
        PlatformItem("Portfolio", user?.portfolioUrl?.ifBlank { null } ?: "", "Personal Website", CyberPurple, "🌐", !user?.portfolioUrl.isNullOrBlank()),
        PlatformItem("Resume", user?.resumeUrl?.ifBlank { null } ?: "", "Resume / CV", StatusSuccess, "📄", !user?.resumeUrl.isNullOrBlank()),
        PlatformItem("HackerRank", if (!user?.hackerrankUsername.isNullOrBlank()) "https://www.hackerrank.com/${user.hackerrankUsername}" else "https://www.hackerrank.com", "Coding Skill Certificates", Color(0xFF2EC4B6), "🎯", !user?.hackerrankUsername.isNullOrBlank()),
        PlatformItem("CodeChef", if (!user?.codechefUsername.isNullOrBlank()) "https://www.codechef.com/users/${user.codechefUsername}" else "https://www.codechef.com", "Competitive Programming", Color(0xFF5B4636), "👨‍🍳", !user?.codechefUsername.isNullOrBlank()),
        PlatformItem("Codeforces", if (!user?.codeforcesUsername.isNullOrBlank()) "https://codeforces.com/profile/${user.codeforcesUsername}" else "https://codeforces.com", "Global CP Contests", Color(0xFF1F8ACB), "🏆", !user?.codeforcesUsername.isNullOrBlank()),
        PlatformItem("Kaggle", if (!user?.kaggleUsername.isNullOrBlank()) "https://www.kaggle.com/${user.kaggleUsername}" else "https://www.kaggle.com", "AI & Data Science Hub", Color(0xFF20BEFF), "📊", !user?.kaggleUsername.isNullOrBlank())
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Connected Platforms & Links",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryText
            )

            Text(
                text = "Edit Profiles",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onConnectAccount() }
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(platforms) { platform ->
                GlassCard(
                    modifier = Modifier
                        .width(155.dp)
                        .clickable {
                            if (platform.isConnected && platform.url.isNotBlank()) {
                                onOpenUrl(platform.url)
                            } else {
                                onConnectAccount()
                            }
                        },
                    cornerRadius = 24.dp,
                    backgroundColor = CardDark,
                    borderColor = if (platform.isConnected) platform.color.copy(alpha = 0.5f) else GlassBorder
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
                            Icon(
                                imageVector = if (platform.isConnected) Icons.Default.CheckCircle else Icons.Default.Launch,
                                contentDescription = "Status",
                                tint = if (platform.isConnected) StatusSuccess else SecondaryText,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(text = platform.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
                        Text(
                            text = if (platform.isConnected) "Connected" else "Not Connected",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (platform.isConnected) StatusSuccess else MutedText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (platform.isConnected && platform.url.isNotBlank()) {
                                    onOpenUrl(platform.url)
                                } else {
                                    onConnectAccount()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = if (platform.isConnected) platform.color else SurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (platform.isConnected) "Open Profile" else "+ Connect",
                                color = if (platform.isConnected) Color.White else ElectricBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
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
    val emoji: String,
    val isConnected: Boolean = false
)

/* ========================================================================= */
/* LINKEDIN SECTION                                                         */
/* ========================================================================= */
@Composable
fun LinkedInSection(
    user: AuthUserModel?,
    onOpenUrl: (String) -> Unit,
    onConnectAccount: () -> Unit,
    viewModel: StudyViewModel
) {
    var aiFeedbackText by remember { mutableStateOf("") }
    var isAnalyzing by remember { mutableStateOf(false) }
    val isConnected = !user?.linkedinUrl.isNullOrBlank()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // LinkedIn Banner
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFF0A66C2).copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF0A66C2)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("in", color = Color.White, fontWeight = FontWeight.Black, fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("LinkedIn Profile", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text(
                                text = if (isConnected) user?.linkedinUrl ?: "Connected" else "Account Not Connected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isConnected) StatusSuccess else MutedText,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isConnected && !user?.linkedinUrl.isNullOrBlank()) {
                                onOpenUrl(user.linkedinUrl)
                            } else {
                                onConnectAccount()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A66C2)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isConnected) "Open Profile" else "Connect LinkedIn", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                            userPrompt = "Review my student LinkedIn profile (${user?.linkedinUrl.ifBlank { "CS Engineering Student" }}). Give me 3 high-impact headline suggestions and 3 tips for my profile summary as a Computer Science student.",
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
/* GITHUB SECTION                                                            */
/* ========================================================================= */
@Composable
fun GitHubSection(
    user: AuthUserModel?,
    onOpenUrl: (String) -> Unit,
    onConnectAccount: () -> Unit,
    authViewModel: AuthViewModel?,
    viewModel: StudyViewModel
) {
    val username = user?.githubUsername ?: ""
    val isConnected = username.isNotBlank()
    val commits = user?.githubCommits ?: 0

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFF2DBA4E).copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Text("🐙", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("GitHub Portfolio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text(
                                text = if (isConnected) "@$username • $commits Commits" else "Account Not Connected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isConnected) StatusSuccess else MutedText,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isConnected) {
                                onOpenUrl("https://github.com/$username")
                            } else {
                                onConnectAccount()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24292E)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isConnected) "Open GitHub" else "Connect GitHub", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Commit Streak Tracker", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                    if (isConnected) {
                        IconButton(onClick = { authViewModel?.fetchLiveGithubStats(username) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (isConnected) "Current Streak: ${user?.codingStreakDays ?: 0} Days ($commits total commits)" else "Connect your GitHub username to track live commit streaks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (commits > 0) StatusSuccess else MutedText,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dynamic Contribution Grid
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    repeat(12) { col ->
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            repeat(5) { row ->
                                val active = commits > 0 && (col + row) % 3 == 0
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
/* LEETCODE SECTION                                                          */
/* ========================================================================= */
@Composable
fun LeetCodeSection(
    user: AuthUserModel?,
    onOpenUrl: (String) -> Unit,
    onConnectAccount: () -> Unit,
    viewModel: StudyViewModel
) {
    val username = user?.leetcodeUsername ?: ""
    val isConnected = username.isNotBlank()
    val totalUserSolved = user?.leetcodeSolved ?: 0

    var selectedDifficultyFilter by remember { mutableStateOf("ALL") }
    val solvedStateMap = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }

    val initialProblems = remember {
        listOf(
            ProblemItem("1", "Two Sum", "EASY", "Array, Hash Table", false),
            ProblemItem("20", "Valid Parentheses", "EASY", "Stack, String", false),
            ProblemItem("200", "Number of Islands", "MEDIUM", "BFS, DFS, Graph", false),
            ProblemItem("146", "LRU Cache", "MEDIUM", "Hash Table, Linked List", false),
            ProblemItem("42", "Trapping Rain Water", "HARD", "Two Pointers, Stack", false)
        )
    }

    val problems = initialProblems.map { item ->
        item.copy(isSolved = solvedStateMap[item.id] ?: false)
    }

    val sessionSolved = problems.count { it.isSolved }
    val totalDisplaySolved = maxOf(totalUserSolved, sessionSolved)

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
            borderColor = Color(0xFFFFA116).copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Text("⚡", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("LeetCode Arena", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text(
                                text = if (isConnected) "@$username • $totalDisplaySolved Problems Solved" else "Account Not Connected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isConnected) StatusSuccess else MutedText,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isConnected) {
                                onOpenUrl("https://leetcode.com/u/$username")
                            } else {
                                onConnectAccount()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA116)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isConnected) "Open LeetCode" else "Connect LeetCode", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Launch, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // Daily Problem Tracker
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 24.dp,
            backgroundColor = CardDark,
            borderColor = GlassBorder
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Daily DSA Practice", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                    Text("Tap to mark solved", style = MaterialTheme.typography.labelSmall, color = MutedText, fontSize = 10.sp)
                }
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
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                solvedStateMap[prob.id] = !(solvedStateMap[prob.id] ?: false)
                            }
                            .padding(vertical = 8.dp, horizontal = 4.dp),
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
/* UNSTOP SECTION                                                            */
/* ========================================================================= */
@Composable
fun UnstopSection(
    user: AuthUserModel?,
    onOpenUrl: (String) -> Unit,
    onConnectAccount: () -> Unit
) {
    val isConnected = !user?.unstopUrl.isNullOrBlank()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFF1B365D).copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Text("🚀", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Unstop Opportunities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text(
                                text = if (isConnected) user?.unstopUrl ?: "Connected" else "Account Not Connected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isConnected) StatusSuccess else MutedText,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (isConnected && !user?.unstopUrl.isNullOrBlank()) {
                                onOpenUrl(user.unstopUrl)
                            } else {
                                onConnectAccount()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B365D)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (isConnected) "Open Unstop" else "Connect Unstop", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Launch, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        Text("Live Hackathons & Opportunities", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val unstopList = listOf(
            UnstopEvent("Flipkart GRID 6.0 Software Challenge", "Hackathon • $10,000 Prize Pool", "Ends in 4 Days", "APPLY"),
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
                        onClick = { onOpenUrl("https://unstop.com") },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
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
/* INSTAGRAM SECTION                                                        */
/* ========================================================================= */
@Composable
fun InstagramSection(onOpenUrl: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = Color(0xFFE4405F).copy(alpha = 0.4f)
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
                        onClick = { onOpenUrl("https://www.instagram.com") },
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
/* OTHER CODING PLATFORMS SECTION                                          */
/* ========================================================================= */
@Composable
fun OtherCodingPlatformsSection(
    user: AuthUserModel?,
    onOpenUrl: (String) -> Unit,
    onConnectAccount: () -> Unit
) {
    val platforms = listOf(
        PlatformItem("HackerRank", if (!user?.hackerrankUsername.isNullOrBlank()) "https://www.hackerrank.com/${user.hackerrankUsername}" else "https://www.hackerrank.com", "Skill Certifications & Python/SQL Tests", Color(0xFF2EC4B6), "🎯", !user?.hackerrankUsername.isNullOrBlank()),
        PlatformItem("CodeChef", if (!user?.codechefUsername.isNullOrBlank()) "https://www.codechef.com/users/${user.codechefUsername}" else "https://www.codechef.com", "Long Challenges & Star Ratings", Color(0xFF5B4636), "👨‍🍳", !user?.codechefUsername.isNullOrBlank()),
        PlatformItem("Codeforces", if (!user?.codeforcesUsername.isNullOrBlank()) "https://codeforces.com/profile/${user.codeforcesUsername}" else "https://codeforces.com", "Div 1, Div 2, Div 3 Global Contests", Color(0xFF1F8ACB), "🏆", !user?.codeforcesUsername.isNullOrBlank()),
        PlatformItem("Kaggle", if (!user?.kaggleUsername.isNullOrBlank()) "https://www.kaggle.com/${user.kaggleUsername}" else "https://www.kaggle.com", "Jupyter Notebooks, Datasets & ML Contests", Color(0xFF20BEFF), "📊", !user?.kaggleUsername.isNullOrBlank()),
        PlatformItem("GeeksforGeeks", "https://www.geeksforgeeks.org", "Data Structures, Algorithms & CS Core", Color(0xFF2F9E44), "📗", true),
        PlatformItem("Microsoft Learn", "https://learn.microsoft.com", "Free Azure, AI & Developer Training", Color(0xFF00A4EF), "💻", true),
        PlatformItem("Google Cloud Skills Boost", "https://www.cloudskillsboost.google", "Free Google Cloud & Android Labs", Color(0xFF4285F4), "☁️", true),
        PlatformItem("freeCodeCamp", "https://www.freecodecamp.org", "Free Web Dev & Python Certifications", Color(0xFF0A0A23), "🔥", true)
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
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Text(plat.emoji, fontSize = 26.sp)
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(plat.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 15.sp)
                            Text(
                                text = if (plat.isConnected) plat.category else "Not Connected - Tap to Link",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (plat.isConnected) SecondaryText else MutedText,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (plat.isConnected && plat.url.isNotBlank()) {
                                onOpenUrl(plat.url)
                            } else {
                                onConnectAccount()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = plat.color),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(34.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        Text(if (plat.isConnected) "Open" else "+ Connect", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
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

    val userCertList = remember { androidx.compose.runtime.mutableStateListOf<CertItem>() }
    var showAddCertDialog by remember { mutableStateOf(false) }

    var newCertTitle by remember { mutableStateOf("") }
    var newCertProvider by remember { mutableStateOf("") }
    var newCertUrl by remember { mutableStateOf("") }
    var newCertIsCompleted by remember { mutableStateOf(true) }

    val completed = userCertList.filter { it.isCompleted }
    val inProgress = userCertList.filter { !it.isCompleted }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            cornerRadius = 28.dp,
            backgroundColor = SurfaceDark,
            borderColor = StatusSuccess.copy(alpha = 0.4f)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📜", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Certifications Tracker & Hub", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text("Track, verify and claim free industry certifications", style = MaterialTheme.typography.bodyMedium, color = MutedText, fontSize = 11.sp)
                        }
                    }

                    Button(
                        onClick = { showAddCertDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("+ Add Cert", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryText)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CertStatPill("Completed", "${completed.size} Certs", StatusSuccess)
                    CertStatPill("In Progress", "${inProgress.size} Active", ElectricBlue)
                    CertStatPill("Next Recommended", "Azure AI", CyberPurple)
                }
            }
        }

        // 1. Completed Certificates
        Text("Completed Certificates (Verified)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        if (completed.isEmpty()) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎓", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No Completed Certificates Yet", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Earn certifications on NPTEL, freeCodeCamp, or Google Cloud and add them to your fresh profile!", style = MaterialTheme.typography.bodySmall, color = SecondaryText, fontSize = 11.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        } else {
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
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 13.sp)
                                Text("${item.provider} • ${item.date}", style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                            }
                        }

                        OutlinedButton(
                            onClick = { onOpenUrl(item.url.ifBlank { "https://google.com" }) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Text("View Badge", fontSize = 10.sp, color = ElectricBlue, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. In Progress Certificates
        Text("Certificates In Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        if (inProgress.isEmpty()) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("⏳", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No Active Certificates In Progress", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryText)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Explore free providers below to enroll in your next certification!", style = MaterialTheme.typography.bodySmall, color = SecondaryText, fontSize = 11.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        } else {
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
                            onClick = { onOpenUrl(item.url.ifBlank { "https://google.com" }) },
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
        }

        if (showAddCertDialog) {
            AlertDialog(
                onDismissRequest = { showAddCertDialog = false },
                title = { Text("Add Certificate", color = PrimaryText, fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = newCertTitle,
                            onValueChange = { newCertTitle = it },
                            label = { Text("Certificate Title") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newCertProvider,
                            onValueChange = { newCertProvider = it },
                            label = { Text("Provider (e.g. Google, NPTEL)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newCertUrl,
                            onValueChange = { newCertUrl = it },
                            label = { Text("Certificate Link / URL") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = newCertIsCompleted,
                                onCheckedChange = { newCertIsCompleted = it }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (newCertIsCompleted) "Completed" else "In Progress", color = PrimaryText, fontSize = 12.sp)
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newCertTitle.isNotBlank()) {
                                userCertList.add(
                                    CertItem(
                                        title = newCertTitle.trim(),
                                        provider = newCertProvider.trim().ifBlank { "Self-Paced" },
                                        date = if (newCertIsCompleted) "Earned Recently" else "In Progress",
                                        url = newCertUrl.trim(),
                                        isCompleted = newCertIsCompleted,
                                        progress = if (newCertIsCompleted) 100 else 50
                                    )
                                )
                                newCertTitle = ""
                                newCertProvider = ""
                                newCertUrl = ""
                                showAddCertDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Text("Add", color = PrimaryText, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showAddCertDialog = false }) {
                        Text("Cancel", color = SecondaryText)
                    }
                },
                containerColor = SurfaceDark,
                shape = RoundedCornerShape(20.dp)
            )
        }

        // Recommended Next Certificate
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

        // Free Certification Providers Grid
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
/* AI CAREER ASSISTANT                                                       */
/* ========================================================================= */
@Composable
fun AiCareerAssistantCard(user: AuthUserModel?, viewModel: StudyViewModel, onOpenAiScreen: () -> Unit) {
    var connectedCount = 0
    if (!user?.linkedinUrl.isNullOrBlank()) connectedCount++
    if (!user?.githubUsername.isNullOrBlank()) connectedCount++
    if (!user?.leetcodeUsername.isNullOrBlank()) connectedCount++
    if (!user?.unstopUrl.isNullOrBlank()) connectedCount++
    if (!user?.portfolioUrl.isNullOrBlank()) connectedCount++
    if (!user?.resumeUrl.isNullOrBlank()) connectedCount++
    if (!user?.hackerrankUsername.isNullOrBlank()) connectedCount++
    if (!user?.codechefUsername.isNullOrBlank()) connectedCount++
    if (!user?.codeforcesUsername.isNullOrBlank()) connectedCount++
    if (!user?.kaggleUsername.isNullOrBlank()) connectedCount++

    val completionPercent = (connectedCount * 10).coerceAtMost(100)

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
                CareerRoadmapPill("Profile Completion", "$completionPercent%", if (completionPercent > 50) StatusSuccess else StatusWarning)
                CareerRoadmapPill("Daily Recommended Platform", "LeetCode (Solve Array / String Easy)", ElectricBlue)
                CareerRoadmapPill("Weekly Goal", "Connect GitHub, LinkedIn & Portfolio Links", CyberPurple)
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
fun AiCareerAssistantDetailed(user: AuthUserModel?, viewModel: StudyViewModel) {
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

                Text("Skill Improvement Suggestions for ${user?.name ?: "Student"}:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
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
/* NOTIFICATIONS & ALERTS SECTION                                           */
/* ========================================================================= */
@Composable
fun CareerNotificationsSection() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Career Alerts & Notifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)

        val alerts = listOf(
            NotificationItem("Welcome to Career Hub!", "Your fresh professional portfolio is ready. Connect GitHub, LinkedIn, and LeetCode to track placement progress.", "Just now", ElectricBlue, Icons.Default.Work),
            NotificationItem("Start Your Coding Streak", "Solve your first problem today on LeetCode or GitHub to initiate your streak counter!", "Just now", FlameOrange, Icons.Default.Code),
            NotificationItem("Placement Readiness", "Complete your profile details to unlock personalized AI Placement Guidance.", "Just now", CyberPurple, Icons.Default.AutoAwesome)
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
