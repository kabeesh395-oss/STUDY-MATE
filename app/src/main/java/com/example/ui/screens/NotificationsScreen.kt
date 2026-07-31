package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.StudyViewModel

data class NotificationItemData(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isUnread: Boolean,
    val section: String // "Today" or "Yesterday"
)

@Composable
fun NotificationsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("All") }

    val notificationsList = remember {
        listOf(
            NotificationItemData(
                id = "1",
                title = "New Study Material",
                message = "Quantum Mechanics notes have been added.",
                timestamp = "10:30 AM",
                isUnread = true,
                section = "Today"
            ),
            NotificationItemData(
                id = "2",
                title = "Quiz Reminder",
                message = "Don't forget! You have a quiz on Electric Circuits.",
                timestamp = "09:15 AM",
                isUnread = true,
                section = "Today"
            ),
            NotificationItemData(
                id = "3",
                title = "Streak Reward",
                message = "Great job! You completed your 7 day streak.",
                timestamp = "Yesterday",
                isUnread = false,
                section = "Yesterday"
            ),
            NotificationItemData(
                id = "4",
                title = "Test Result",
                message = "You scored 85% in Electronic Devices Test.",
                timestamp = "Yesterday",
                isUnread = false,
                section = "Yesterday"
            )
        )
    }

    val filteredList = remember(selectedTab) {
        if (selectedTab == "Unread") {
            notificationsList.filter { it.isUnread }
        } else {
            notificationsList
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo("HOME") },
                    modifier = Modifier.testTag("notifications_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryText
                    )
                }

                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryText
                )

                IconButton(
                    onClick = { viewModel.navigateTo("SETTINGS") },
                    modifier = Modifier.testTag("notifications_settings_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = SecondaryText
                    )
                }
            }

            // Filter Tabs ("All", "Unread")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                listOf("All", "Unread").forEach { tab ->
                    val isSelected = selectedTab == tab
                    Column(
                        modifier = Modifier
                            .clickable { selectedTab = tab }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = tab,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) PrimaryText else SecondaryText,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (isSelected) ElectricBlue else Color.Transparent)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Notifications Grouped List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val todayItems = filteredList.filter { it.section == "Today" }
                val yesterdayItems = filteredList.filter { it.section == "Yesterday" }

                if (todayItems.isNotEmpty()) {
                    item {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryText,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(todayItems, key = { it.id }) { item ->
                        NotificationCard(item = item)
                    }
                }

                if (yesterdayItems.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Yesterday",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryText,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    items(yesterdayItems, key = { it.id }) { item ->
                        NotificationCard(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(item: NotificationItemData) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 16.dp,
        backgroundColor = CardDark,
        borderColor = GlassBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dark Box with White Lightning 'S' Logo
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgDark)
                    .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lightning_s_logo),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText,
                        fontSize = 14.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.timestamp,
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryText,
                            fontSize = 11.sp
                        )
                        if (item.isUnread) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText,
                    fontSize = 12.sp
                )
            }
        }
    }
}
