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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.UploadFile
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entities.UnitEntity
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPink
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun SubjectDetailScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val subject by viewModel.selectedSubject.collectAsState()
    val units by viewModel.unitsForSelectedSubject.collectAsState()

    val sub = subject
    if (sub == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No subject selected", color = SecondaryText)
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Hero Header
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(listOf(ElectricBlue, CyberPurple, BgDark)))
                    )

                    // Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.3f),
                                        BgDark
                                    )
                                )
                            )
                    )

                    // Top Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 12.dp, end = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.navigateTo("HOME") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CardDark.copy(alpha = 0.8f))
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                        }

                        Button(
                            onClick = { viewModel.navigateTo("UPLOAD") },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = "Upload", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload Notes", color = PrimaryText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Hero Text
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = sub.name,
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${sub.code} • ${sub.semester} • Completion ${sub.completionPercentage}%",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElectricBlueLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Units Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Syllabus & Units",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    Text(
                        text = "${units.size} Units",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText
                    )
                }
            }

            // Unit Cards
            items(units) { unit ->
                UnitCardItem(
                    unit = unit,
                    onActionClick = { action ->
                        viewModel.selectUnit(unit)
                        when (action) {
                            "UPLOAD" -> viewModel.navigateTo("UPLOAD")
                            "AI_SUMMARY" -> viewModel.navigateTo("AI_CHAT")
                            "EXAM" -> viewModel.navigateTo("EXAM_MODE")
                            "QUIZ" -> viewModel.navigateTo("QUIZ")
                            "FLASHCARDS" -> viewModel.navigateTo("FLASHCARDS")
                            "REVISION" -> viewModel.navigateTo("REVISION")
                            else -> viewModel.navigateTo("AI_CHAT")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun UnitCardItem(
    unit: UnitEntity,
    onActionClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("unit_card_${unit.unitNumber}"),
        backgroundColor = CardDark,
        borderColor = GlassBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ElectricBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "U${unit.unitNumber}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = ElectricBlue
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = unit.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Progress: ${unit.completionPercentage}%",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 11.sp,
                            color = SecondaryText
                        )
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    // Actions Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        UnitActionButton("Upload Notes", Icons.Default.UploadFile) { onActionClick("UPLOAD") }
                        UnitActionButton("AI Summary", Icons.Default.AutoAwesome) { onActionClick("AI_SUMMARY") }
                        UnitActionButton("Exam Mode", Icons.Default.Description) { onActionClick("EXAM") }
                        UnitActionButton("Quiz", Icons.Default.Quiz) { onActionClick("QUIZ") }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        UnitActionButton("Flashcards", Icons.Default.Style) { onActionClick("FLASHCARDS") }
                        UnitActionButton("Mind Map", Icons.Default.Psychology) { onActionClick("AI_SUMMARY") }
                        UnitActionButton("Formulas", Icons.Default.Functions) { onActionClick("AI_SUMMARY") }
                        UnitActionButton("Voice AI", Icons.Default.Mic) { onActionClick("AI_SUMMARY") }
                    }
                }
            }
        }
    }
}

@Composable
fun UnitActionButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(76.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceDark)
            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = label, tint = ElectricBlueLight, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryText
            )
        }
    }
}
