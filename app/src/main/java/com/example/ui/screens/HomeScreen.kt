package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entities.SubjectEntity
import com.example.ui.components.AnimatedBarChart
import com.example.ui.components.AnimatedStreakFlame
import com.example.ui.components.BenDailyWelcomeDialog
import com.example.ui.components.FloatingBenWidget
import com.example.ui.components.GlassCard
import com.example.ui.components.ProgressRing
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
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showDailyWelcomeDialog by remember { mutableStateOf(false) }
    val subjectsList by viewModel.subjects.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    val fabTransition = rememberInfiniteTransition(label = "fabPulse")
    val fabScale by fabTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "fabScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 90.dp, top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Bold Header Section
            item {
                val hour = remember { java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY) }
                val (timeGreeting, timeIcon) = when (hour) {
                    in 5..11 -> "Good Morning" to "☀️"
                    in 12..16 -> "Good Afternoon" to "🌤️"
                    in 17..21 -> "Good Evening" to "🌅"
                    else -> "Good Night" to "🌙"
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    // Minimal Top App Bar with Centered Logo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.navigateTo("SETTINGS") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(SurfaceDark)
                                .border(1.dp, GlassBorder, CircleShape)
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryText)
                        }

                        // Centered Lightning "S" Logo
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(BgDark)
                                .border(1.dp, GlassBorder, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_lightning_s_logo),
                                contentDescription = "StudyMate AI",
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Quick Theme Switcher Button (Sun/Moon)
                            IconButton(
                                onClick = { viewModel.toggleThemeMode() },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                                    .testTag("theme_toggle_btn")
                            ) {
                                Text(
                                    text = if (themeMode == "LIGHT") "☀️" else "🌙",
                                    fontSize = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = { viewModel.navigateTo("SEARCH") },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(SurfaceDark)
                                    .border(1.dp, GlassBorder, CircleShape)
                                    .testTag("home_search_btn")
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimaryText)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Notification Bell with Unread Badge
                            Box {
                                IconButton(
                                    onClick = { viewModel.navigateTo("NOTIFICATIONS") },
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(SurfaceDark)
                                        .border(1.dp, GlassBorder, CircleShape)
                                        .testTag("home_notifications_btn")
                                ) {
                                    Icon(
                                        imageVector = androidx.compose.material.icons.Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = PrimaryText
                                    )
                                }
                                // Unread Badge
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(ElectricBlue)
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // User Welcome Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Hi, Kabeesh 👋",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = PrimaryText
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Let's continue your learning journey",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SecondaryText,
                                fontSize = 13.sp
                            )
                        }

                        // Avatar with Blue Gradient Border
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(ElectricBlue, ElectricBlueDark)))
                                .padding(1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(11.dp))
                                    .background(BgDark)
                                    .clickable { viewModel.navigateTo("PROFILE") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "K",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black,
                                    color = ElectricBlue
                                )
                            }
                        }
                    }
                }
            }

            // Daily Motivation Quote Banner
            item {
                val quotes = remember {
                    listOf(
                        "\"The secret of getting ahead is getting started.\" — Mark Twain",
                        "\"Success is the sum of small efforts repeated day in and day out.\"",
                        "\"Don't watch the clock; do what it does. Keep going.\" — Sam Levenson",
                        "\"Your future is created by what you do today, not tomorrow.\""
                    )
                }
                val todayQuote = remember { quotes[java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR) % quotes.size] }

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
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = todayQuote,
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Dashboard Stats Card (32.dp rounded, dark background)
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 32.dp,
                    backgroundColor = SurfaceDark,
                    borderColor = GlassBorder
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProgressRing(percentage = 72, radius = 44.dp, strokeWidth = 8.dp)

                        Spacer(modifier = Modifier.width(18.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            // Streak pill with pulsing fire
                            AnimatedStreakFlame(streakDays = 12)

                            Spacer(modifier = Modifier.height(12.dp))

                            // Current goal container
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0x0DFFFFFF))
                                    .padding(12.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "CURRENT GOAL",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MutedText
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Finish AI Unit 3",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Access Section
            item {
                Column {
                    Text(
                        text = "Quick Access",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickAccessTile(
                            label = "Notes",
                            icon = Icons.Default.Book,
                            containerColor = ElectricBlue,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo("SEARCH") }
                        )
                        QuickAccessTile(
                            label = "Questions",
                            icon = Icons.Default.Folder,
                            containerColor = StatusSuccess,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo("QUESTION_BANK") }
                        )
                        QuickAccessTile(
                            label = "Documents",
                            icon = Icons.Default.Storage,
                            containerColor = CyberPurple,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo("UPLOAD") }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        QuickAccessTile(
                            label = "AI Tutor",
                            icon = Icons.Default.Psychology,
                            containerColor = StatusWarning,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo("AI_CHAT") }
                        )
                        QuickAccessTile(
                            label = "Flashcards",
                            icon = Icons.Default.AutoAwesome,
                            containerColor = CyberPink,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo("FLASHCARDS") }
                        )
                        QuickAccessTile(
                            label = "Bookmarks",
                            icon = Icons.Default.Bookmark,
                            containerColor = FlameOrange,
                            modifier = Modifier.weight(1f),
                            onClick = { viewModel.navigateTo("REVISION") }
                        )
                    }
                }
            }

            // Question Paper Bank Feature Banner Card
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo("QUESTION_BANK") }
                        .testTag("home_question_bank_banner"),
                    cornerRadius = 24.dp,
                    backgroundColor = CardDark,
                    borderColor = ElectricBlue.copy(alpha = 0.6f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(ElectricBlue.copy(alpha = 0.15f))
                                    .border(1.dp, ElectricBlue, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = "Question Paper Bank",
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Question Paper Bank",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(ElectricBlue)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text("AI ENHANCED", color = Color.Black, fontWeight = FontWeight.Black, fontSize = 8.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "IA 1, IA 2, Model & Semester papers with Gemini AI mark categorization & answers",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SecondaryText,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go",
                            tint = ElectricBlue,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer { rotationZ = 180f }
                        )
                    }
                }
            }

            // Weekly Progress Graph Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    cornerRadius = 28.dp,
                    backgroundColor = SurfaceDark,
                    borderColor = GlassBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Weekly Activity",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryText
                                )
                                Text(
                                    text = "28.5 hrs logged this week",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MutedText,
                                    fontSize = 12.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(StatusSuccess.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+18% vs LAST WEEK",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = StatusSuccess,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        val weeklyData = listOf(
                            "Mon" to 0.4f,
                            "Tue" to 0.6f,
                            "Wed" to 0.9f,
                            "Thu" to 0.3f,
                            "Fri" to 0.5f,
                            "Sat" to 0.75f,
                            "Sun" to 0.4f
                        )
                        AnimatedBarChart(data = weeklyData)
                    }
                }
            }

            // Subjects Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Subjects",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    TextButton(onClick = { viewModel.navigateTo("SEARCH") }) {
                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = ElectricBlue
                        )
                    }
                }
            }

            // Subjects Cards List
            item {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    subjectsList.forEach { subject ->
                        SubjectCardItem(
                            subject = subject,
                            onSelect = {
                                viewModel.selectSubject(subject)
                                viewModel.navigateTo("SUBJECT_DETAIL")
                            },
                            onAiClick = {
                                viewModel.selectSubject(subject)
                                viewModel.navigateTo("AI_CHAT")
                            },
                            onToggleFavorite = { viewModel.toggleFavoriteSubject(subject) },
                            onDelete = { viewModel.deleteSubject(subject.id) }
                        )
                    }
                }
            }
        }

        // Floating Action Button (+ Create Subject) with gentle pulsing scale
        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .scale(fabScale)
                .testTag("create_subject_fab"),
            containerColor = ElectricBlue,
            contentColor = PrimaryText,
            shape = RoundedCornerShape(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create Subject"
            )
        }

        // Ben AI Companion Floating Floating Action Widget
        FloatingBenWidget(
            onOpenBenTutor = { showDailyWelcomeDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd)
        )

        // Ben Daily Welcome Dialog
        if (showDailyWelcomeDialog) {
            BenDailyWelcomeDialog(
                streakDays = 12,
                onClaimReward = { xp, coins ->
                    viewModel.addXpAndCoins(xp, coins)
                    viewModel.navigateTo("AI_CHAT")
                },
                onDismiss = { showDailyWelcomeDialog = false }
            )
        }

        // Create Subject Modal Dialog
        if (showCreateDialog) {
            CreateSubjectDialog(
                onDismiss = { showCreateDialog = false },
                onCreate = { name, code, sem, category ->
                    viewModel.createSubject(name, code, sem, category)
                    showCreateDialog = false
                }
            )
        }
    }
}

@Composable
fun ShortcutPill(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = Modifier
            .width(82.dp)
            .height(72.dp),
        cornerRadius = 20.dp,
        backgroundColor = SurfaceDark,
        borderColor = GlassBorder,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = ElectricBlue, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 10.sp,
                color = SecondaryText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubjectCardItem(
    subject: SubjectEntity,
    onSelect: () -> Unit,
    onAiClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("subject_card_${subject.id}"),
        cornerRadius = 28.dp,
        backgroundColor = CardDark,
        borderColor = if (subject.isFavorite) ElectricBlue.copy(alpha = 0.5f) else GlassBorder,
        onClick = onSelect
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                when (subject.iconCategory) {
                                    "AI" -> ElectricBlue
                                    "CODE" -> CyberPurple
                                    "DATA" -> Color(0xFFD97706)
                                    "MATH" -> CyberPink
                                    else -> ElectricBlue
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (subject.iconCategory) {
                                "AI" -> Icons.Default.Psychology
                                "CODE" -> Icons.Default.Terminal
                                "DATA" -> Icons.Default.Storage
                                "MATH" -> Icons.Default.Functions
                                else -> Icons.Default.Book
                            },
                            contentDescription = subject.name,
                            tint = PrimaryText,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = subject.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "${subject.code} • ${subject.semester}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 11.sp,
                            color = SecondaryText
                        )
                    }
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = SecondaryText)
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(SurfaceDark)
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (subject.isFavorite) "Unfavorite" else "Favorite", color = PrimaryText) },
                            leadingIcon = { Icon(Icons.Default.Favorite, contentDescription = null, tint = ElectricBlue) },
                            onClick = {
                                onToggleFavorite()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Subject", color = MaterialTheme.colorScheme.error) },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                            onClick = {
                                onDelete()
                                showMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Completion Progress Bar
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(Color(0x0DFFFFFF))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(subject.completionPercentage / 100f)
                            .clip(CircleShape)
                            .background(
                                if (subject.iconCategory == "CODE") CyberPurple else ElectricBlue
                            )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${subject.completionPercentage}% Complete",
                        style = MaterialTheme.typography.labelMedium,
                        color = MutedText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onSelect,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Continue", color = PrimaryText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onAiClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "AI", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("AI Tutor", color = PrimaryText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CreateSubjectDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, code: String, sem: String, category: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("Semester 5") }
    var category by remember { mutableStateOf("AI") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create New Subject", color = PrimaryText, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Subject Name (e.g. Compiler Design)", color = SecondaryText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_subject_name")
                )
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Subject Code (e.g. CS8592)", color = SecondaryText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_subject_code")
                )
                OutlinedTextField(
                    value = semester,
                    onValueChange = { semester = it },
                    label = { Text("Semester", color = SecondaryText) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) onCreate(name, code, semester, category)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Create Subject", color = PrimaryText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SecondaryText)
            }
        },
        containerColor = SurfaceDark
    )
}

@Composable
fun QuickAccessTile(
    label: String,
    icon: ImageVector,
    containerColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier.height(84.dp),
        cornerRadius = 16.dp,
        backgroundColor = CardDark,
        borderColor = GlassBorder,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

