package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.StatusDanger
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign

@Composable
fun RevisionPlannerScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf("TODAY") }
    val allNotes by viewModel.notes.collectAsState()

    val revisionTasks = remember(allNotes) {
        allNotes.mapIndexed { index, note ->
            RevisionTaskItem(
                id = note.id,
                topic = note.title,
                priority = if (index % 2 == 0) "High Priority" else "Medium Priority",
                time = "Scheduled Today",
                isCompleted = false,
                xp = 20
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
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
                        text = "Smart Revision Schedule",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    Text(
                        text = "Spaced Repetition (1, 3, 7, 30 Days)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CardDark)
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "Reminders", tint = ElectricBlue)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Revision Algorithm Banner
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = CardDark,
                borderColor = GlassBorder
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Schedule, contentDescription = null, tint = ElectricBlue)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Ebbinghaus Forgetting Curve Alert",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "3 topics are due for 7-day memory retention refresh.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Tasks List
            if (revisionTasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 20.dp, end = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No revision tasks scheduled.\n\nUpload study notes to automatically generate spaced-repetition revision tasks.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SecondaryText,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(revisionTasks) { task ->
                        var isChecked by remember { mutableStateOf(task.isCompleted) }

                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = if (isChecked) SurfaceDark else CardDark,
                            borderColor = if (isChecked) StatusSuccess.copy(alpha = 0.4f) else GlassBorder
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = {
                                        isChecked = !isChecked
                                        if (isChecked) viewModel.addXpPoints(task.xp)
                                    }) {
                                        Icon(
                                            imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.Schedule,
                                            contentDescription = "Check",
                                            tint = if (isChecked) StatusSuccess else ElectricBlue
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = task.topic,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isChecked) SecondaryText else PrimaryText,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "${task.time} • +${task.xp} XP",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = ElectricBlueLight,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when (task.priority) {
                                                "High Priority" -> StatusDanger.copy(alpha = 0.2f)
                                                "Medium Priority" -> StatusWarning.copy(alpha = 0.2f)
                                                else -> SurfaceDark
                                            }
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = task.priority,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = when (task.priority) {
                                            "High Priority" -> StatusDanger
                                            "Medium Priority" -> StatusWarning
                                            else -> SecondaryText
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
    }
}

data class RevisionTaskItem(
    val id: String,
    val topic: String,
    val priority: String,
    val time: String,
    val isCompleted: Boolean,
    val xp: Int
)
