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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
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

    var showAddUnitDialog by remember { mutableStateOf(false) }
    var editingUnit by remember { mutableStateOf<UnitEntity?>(null) }

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
                    Column {
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

                    Button(
                        onClick = { showAddUnitDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Unit", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Unit", color = PrimaryText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Unit Cards
            items(units) { unit ->
                UnitCardItem(
                    unit = unit,
                    onEdit = { editingUnit = unit },
                    onDelete = { viewModel.deleteUnit(unit.id) },
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

        if (showAddUnitDialog) {
            AddUnitDialog(
                onDismiss = { showAddUnitDialog = false },
                onAdd = { title ->
                    viewModel.addUnit(sub.id, title)
                    showAddUnitDialog = false
                }
            )
        }

        if (editingUnit != null) {
            EditUnitDialog(
                unit = editingUnit!!,
                onDismiss = { editingUnit = null },
                onSave = { updated ->
                    viewModel.updateUnit(updated)
                    editingUnit = null
                }
            )
        }
    }
}

@Composable
fun UnitCardItem(
    unit: UnitEntity,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onActionClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Unit Options", tint = SecondaryText)
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(SurfaceDark)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Title", color = PrimaryText) },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = ElectricBlue) },
                            onClick = {
                                onEdit()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Unit", color = MaterialTheme.colorScheme.error) },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                            onClick = {
                                onDelete()
                                showMenu = false
                            }
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

@Composable
fun AddUnitDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String) -> Unit
) {
    var title by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Unit", color = PrimaryText, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Unit Title (e.g. Deep Learning & Neural Nets)", color = SecondaryText) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) onAdd(title)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Add Unit", color = PrimaryText)
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
fun EditUnitDialog(
    unit: UnitEntity,
    onDismiss: () -> Unit,
    onSave: (UnitEntity) -> Unit
) {
    var title by remember { mutableStateOf(unit.title) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Unit ${unit.unitNumber}", color = PrimaryText, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Unit Title", color = SecondaryText) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) onSave(unit.copy(title = title.trim()))
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Save Changes", color = PrimaryText)
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
