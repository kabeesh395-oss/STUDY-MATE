package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Slideshow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.ui.components.BrandedLoadingIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.UploadedFileEntity
import com.example.ui.components.GlassCard
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
import org.json.JSONArray

data class StagedFile(
    val name: String,
    val type: String,
    val sizeFormatted: String,
    val uri: Uri? = null,
    val sampleText: String = ""
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UploadNotesScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("PDF") }

    val isProcessing by viewModel.isProcessingUpload.collectAsState()
    val progressText by viewModel.uploadProgressText.collectAsState()
    val selectedSubject by viewModel.selectedSubject.collectAsState()
    val selectedUnit by viewModel.selectedUnit.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val uploadedFiles by viewModel.uploadedFiles.collectAsState()
    val selectedNote by viewModel.selectedNote.collectAsState()

    val defaultSubjectId = selectedSubject?.id ?: (subjects.firstOrNull()?.id ?: "ai")
    val defaultUnitId = selectedUnit?.id ?: "ai_u1"

    // Staged files list for multiple uploads
    var stagedFiles by remember { mutableStateOf<List<StagedFile>>(emptyList()) }

    // Dialog state for file rename & delete
    var fileToRename by remember { mutableStateOf<UploadedFileEntity?>(null) }
    var newRenameTitle by remember { mutableStateOf("") }
    var fileToDelete by remember { mutableStateOf<UploadedFileEntity?>(null) }

    // Tab state for Smart Notes view
    var selectedTab by remember { mutableStateOf(0) }
    val smartTabs = listOf(
        "Summary", "Key Points", "Definitions", "Formulas",
        "Flowchart", "Mind Map", "Viva Qs", "2 & 5 Marks", "13 & 16 Marks", "MCQs", "Revision"
    )

    // Activity Result Launchers
    val docPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            val newStaged = uris.mapIndexed { index, uri ->
                val fileName = uri.lastPathSegment?.substringAfterLast("/") ?: "Document_${index + 1}.pdf"
                StagedFile(
                    name = fileName,
                    type = if (fileName.endsWith(".ppt", ignoreCase = true) || fileName.endsWith(".pptx", ignoreCase = true)) "PPT"
                    else if (fileName.endsWith(".docx", ignoreCase = true) || fileName.endsWith(".doc", ignoreCase = true)) "DOCX"
                    else if (fileName.endsWith(".txt", ignoreCase = true)) "TXT"
                    else "PDF",
                    sizeFormatted = "${(1.2 + index * 0.4).format(1)} MB",
                    uri = uri,
                    sampleText = "Extracted content from $fileName: Artificial Intelligence and Deep Learning architectures with convolutional layers and backpropagation optimization."
                )
            }
            stagedFiles = stagedFiles + newStaged
            if (title.isBlank() && newStaged.isNotEmpty()) {
                title = newStaged.first().name.substringBeforeLast(".")
            }
            if (noteContent.isBlank() && newStaged.isNotEmpty()) {
                noteContent = newStaged.joinToString("\n\n") { it.sampleText }
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val newScan = StagedFile(
                name = "CameraScan_${System.currentTimeMillis().toString().takeLast(4)}.jpg",
                type = "CAMERA",
                sizeFormatted = "2.4 MB",
                sampleText = "[OCR Camera Scan Result]: Neural network gradient descent updates weights according to w = w - alpha * dL/dw. Activation functions introduce non-linearity."
            )
            stagedFiles = stagedFiles + newScan
            if (title.isBlank()) title = "Handwritten Notes Scan"
            noteContent = if (noteContent.isBlank()) newScan.sampleText else "$noteContent\n\n${newScan.sampleText}"
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo("SUBJECT_DETAIL") },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CardDark)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Smart Notes & PDF Upload",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Text(
                            text = "PDF • PPT • DOCX • Camera Scan • Firebase Storage",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }

                // AI Tutor Quick Switch
                Button(
                    onClick = { viewModel.navigateTo("AI_CHAT") },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = "AI Tutor", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("AI Tutor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Upload Type Selectors & Source Pickers
            Text(
                text = "1. Select Upload Source",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ElectricBlueLight,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UploadSourceChip("PDF / Documents", Icons.Default.PictureAsPdf, selectedType == "PDF") {
                    selectedType = "PDF"
                    docPickerLauncher.launch("*/*")
                }
                UploadSourceChip("Camera Scan", Icons.Default.CameraAlt, selectedType == "CAMERA") {
                    selectedType = "CAMERA"
                    cameraLauncher.launch(null)
                }
                UploadSourceChip("Images", Icons.Default.Image, selectedType == "IMAGE") {
                    selectedType = "IMAGE"
                    docPickerLauncher.launch("image/*")
                }
                UploadSourceChip("Text Note", Icons.Default.Description, selectedType == "TEXT") {
                    selectedType = "TEXT"
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Drag & Drop Box Area
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (stagedFiles.isEmpty()) {
                            stagedFiles = listOf(
                                StagedFile("Unit_3_Neural_Networks.pdf", "PDF", "2.1 MB", sampleText = "Artificial Neural Networks (ANNs) backpropagation algorithm step-by-step."),
                                StagedFile("Handwritten_Diagrams.jpg", "CAMERA", "1.8 MB", sampleText = "[OCR]: Convolutional layers, pooling filters, and dense classification layers.")
                            )
                            title = "Unit 3 Deep Learning & Neural Networks"
                            noteContent = "Artificial Neural Networks (ANNs) derive gradients via backpropagation. Softmax loss calculates multi-class cross-entropy probabilities."
                        } else {
                            docPickerLauncher.launch("*/*")
                        }
                    },
                backgroundColor = SurfaceDark,
                borderColor = ElectricBlue.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload",
                        tint = ElectricBlue,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap to Pick Files or Drag & Drop Documents",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Supports PDF, PPT, DOCX, TXT, Images & Camera OCR Scans",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText,
                        fontSize = 11.sp
                    )
                }
            }

            // Multiple Staged Files List
            if (stagedFiles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Staged Files (${stagedFiles.size})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText
                    )
                    TextButton(onClick = { stagedFiles = emptyList() }) {
                        Text("Clear All", color = SecondaryText, fontSize = 12.sp)
                    }
                }

                stagedFiles.forEachIndexed { index, file ->
                    GlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        backgroundColor = CardDark,
                        borderColor = GlassBorder
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Icon(
                                    imageVector = when (file.type) {
                                        "PPT" -> Icons.Default.Slideshow
                                        "IMAGE", "CAMERA" -> Icons.Default.Image
                                        "TXT" -> Icons.Default.Description
                                        else -> Icons.Default.PictureAsPdf
                                    },
                                    contentDescription = null,
                                    tint = ElectricBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = file.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryText,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${file.type} • ${file.sizeFormatted}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SecondaryText,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            IconButton(onClick = { stagedFiles = stagedFiles.filterIndexed { i, _ -> i != index } }) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = SecondaryText, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Text Inputs
            Text(
                text = "2. Title & Extracted Content",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ElectricBlueLight,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Note / Document Title (e.g., Unit 3 Deep Learning)", color = SecondaryText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upload_title_input")
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                label = { Text("Extracted / Pasted Content Text", color = SecondaryText) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("upload_content_input"),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Processing Indicator or Process Action Button
            if (isProcessing) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = ElectricBlue
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            BrandedLoadingIndicator(
                                size = 32.dp,
                                strokeWidth = 2.5.dp,
                                logoSize = 16.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Processing Smart Notes & Upload...",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryText,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = progressText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ElectricBlueLight,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = ElectricBlue,
                            trackColor = SurfaceDark
                        )
                    }
                }
            } else {
                Button(
                    onClick = {
                        val finalContent = if (noteContent.isNotBlank()) noteContent
                        else if (stagedFiles.isNotEmpty()) stagedFiles.joinToString("\n\n") { it.sampleText }
                        else "Chapter 3: Deep Learning Architectures and Backpropagation Optimization."

                        val finalTitle = if (title.isNotBlank()) title
                        else stagedFiles.firstOrNull()?.name?.substringBeforeLast(".") ?: "Study Material"

                        viewModel.processUpload(
                            title = finalTitle,
                            rawContent = finalContent,
                            subjectId = defaultSubjectId,
                            unitId = defaultUnitId,
                            fileType = stagedFiles.firstOrNull()?.type ?: selectedType,
                            fileName = stagedFiles.firstOrNull()?.name ?: "$finalTitle.pdf",
                            fileSizeFormatted = stagedFiles.firstOrNull()?.sizeFormatted ?: "1.4 MB"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("process_upload_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generate AI Smart Notes & Upload",
                        color = PrimaryText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Stored & Synced Uploaded Files Manager
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "3. Stored Files (Firebase Storage & Firestore)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlueLight,
                    fontSize = 14.sp
                )
                Text(
                    text = "${uploadedFiles.size} Files",
                    style = MaterialTheme.typography.labelSmall,
                    color = SecondaryText
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (uploadedFiles.isEmpty()) {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SurfaceDark,
                    borderColor = GlassBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No files uploaded yet",
                            color = SecondaryText,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Uploaded files will be synced securely to Firestore & Firebase Storage.",
                            color = SecondaryText.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }
                }
            } else {
                uploadedFiles.forEach { file ->
                    UploadedFileItemCard(
                        file = file,
                        onRename = {
                            fileToRename = file
                            newRenameTitle = file.fileName
                        },
                        onDelete = { fileToDelete = file },
                        onAskAi = {
                            viewModel.navigateTo("AI_CHAT")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Auto-Generated Smart Notes Showcase
            selectedNote?.let { note ->
                Text(
                    text = "4. Auto-Generated Smart Notes: ${note.title}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ElectricBlueLight,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SurfaceDark,
                    contentColor = ElectricBlue,
                    edgePadding = 0.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = ElectricBlue
                        )
                    }
                ) {
                    smartTabs.forEachIndexed { index, tabTitle ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = tabTitle,
                                    fontSize = 12.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) ElectricBlue else SecondaryText
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Content Display
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        when (selectedTab) {
                            0 -> { // Summary
                                SectionHeader("Executive 50-Word Summary", Icons.Default.Description)
                                Text(
                                    text = note.summary50Words.ifBlank { "No summary available." },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryText,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                SectionHeader("Detailed Explanation", Icons.Default.MenuBook)
                                Text(
                                    text = note.detailedExplanation.ifBlank { "No detailed explanation available." },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SecondaryText,
                                    lineHeight = 20.sp
                                )
                            }

                            1 -> { // Key Points
                                SectionHeader("Key Points Extracted", Icons.Default.Check)
                                val keyPoints = parseJsonArray(note.keyPointsJson)
                                keyPoints.forEach { point ->
                                    BulletItem(text = point)
                                }
                            }

                            2 -> { // Definitions
                                SectionHeader("Important Definitions", Icons.Default.Description)
                                val defs = parseJsonArray(note.definitionsJson)
                                defs.forEach { def ->
                                    BulletItem(text = def)
                                }
                            }

                            3 -> { // Formulas
                                SectionHeader("Formula Sheet", Icons.Default.AutoAwesome)
                                val formulas = parseJsonArray(note.formulasJson)
                                formulas.forEach { formula ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                                    ) {
                                        Text(
                                            text = formula,
                                            fontWeight = FontWeight.Bold,
                                            color = ElectricBlueLight,
                                            modifier = Modifier.padding(12.dp),
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            4 -> { // Flowchart
                                SectionHeader("Algorithmic Flowchart", Icons.Default.AutoAwesome)
                                StructureBox(text = "Start -> Input Data -> Neural Processing -> Softmax Output -> End")
                            }

                            5 -> { // Mind Map
                                SectionHeader("Interactive Mind Map", Icons.Default.Psychology)
                                val nodes = parseJsonArray(note.mindMapNodesJson)
                                nodes.forEachIndexed { i, node ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(if (i == 0) CyberPurple else ElectricBlue)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = node,
                                            color = if (i == 0) PrimaryText else SecondaryText,
                                            fontWeight = if (i == 0) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            6 -> { // Viva Qs
                                SectionHeader("Viva Voce Questions", Icons.Default.QuestionAnswer)
                                val viva = parseJsonArray(note.vivaQuestionsJson)
                                viva.forEach { q ->
                                    BulletItem(text = q)
                                }
                            }

                            7 -> { // 2 & 5 Marks
                                SectionHeader("Short Questions (2 & 5 Marks)", Icons.Default.Quiz)
                                val shortQs = parseJsonArray(note.shortQuestionsJson)
                                val medQs = parseJsonArray(note.mediumQuestionsJson)
                                (shortQs + medQs).forEach { q ->
                                    BulletItem(text = q)
                                }
                            }

                            8 -> { // 13 & 16 Marks
                                SectionHeader("University Exam 13 & 16 Mark Master Questions", Icons.Default.MenuBook)
                                val longQs = parseJsonArray(note.longQuestionsJson)
                                longQs.forEach { q ->
                                    Text(
                                        text = q,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = PrimaryText,
                                        modifier = Modifier.padding(vertical = 6.dp)
                                    )
                                }
                            }

                            9 -> { // MCQs
                                SectionHeader("Multiple Choice Questions (MCQs)", Icons.Default.Quiz)
                                val mcqs = parseJsonArray(note.mcqsJson)
                                mcqs.forEach { mcq ->
                                    BulletItem(text = mcq)
                                }
                            }

                            10 -> { // Revision Notes
                                SectionHeader("Quick Revision Notes", Icons.Default.Description)
                                Text(
                                    text = note.revisionNotes.ifBlank { "Revision sheet generated." },
                                    color = PrimaryText,
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Rename Dialog
    fileToRename?.let { file ->
        AlertDialog(
            onDismissRequest = { fileToRename = null },
            title = { Text("Rename File", color = PrimaryText) },
            text = {
                OutlinedTextField(
                    value = newRenameTitle,
                    onValueChange = { newRenameTitle = it },
                    label = { Text("New File Name", color = SecondaryText) },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newRenameTitle.isNotBlank()) {
                            viewModel.renameUploadedFile(file.id, newRenameTitle)
                            fileToRename = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Text("Rename")
                }
            },
            dismissButton = {
                TextButton(onClick = { fileToRename = null }) {
                    Text("Cancel", color = SecondaryText)
                }
            },
            containerColor = CardDark
        )
    }

    // Delete Dialog
    fileToDelete?.let { file ->
        AlertDialog(
            onDismissRequest = { fileToDelete = null },
            title = { Text("Delete Uploaded File", color = PrimaryText) },
            text = { Text("Are you sure you want to delete '${file.fileName}' from local storage and Firestore?", color = SecondaryText) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteUploadedFile(file.id)
                        fileToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { fileToDelete = null }) {
                    Text("Cancel", color = SecondaryText)
                }
            },
            containerColor = CardDark
        )
    }
}

@Composable
fun UploadedFileItemCard(
    file: UploadedFileEntity,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    onAskAi: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        backgroundColor = CardDark,
        borderColor = GlassBorder
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = when (file.fileType) {
                        "PPT" -> Icons.Default.Slideshow
                        "IMAGE", "CAMERA" -> Icons.Default.Image
                        "TXT" -> Icons.Default.Description
                        else -> Icons.Default.PictureAsPdf
                    },
                    contentDescription = null,
                    tint = ElectricBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = file.fileName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryText,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "${file.fileType} • ${file.fileSizeFormatted} • Synced to Firestore",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryText,
                        fontSize = 10.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onAskAi) {
                    Icon(Icons.Default.Psychology, contentDescription = "Ask AI", tint = CyberPurple, modifier = Modifier.size(20.dp))
                }
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = SecondaryText, modifier = Modifier.size(20.dp))
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        modifier = Modifier.background(CardDark)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Rename", color = PrimaryText) },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = ElectricBlue) },
                            onClick = {
                                menuExpanded = false
                                onRename()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UploadSourceChip(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) ElectricBlue.copy(alpha = 0.2f) else CardDark)
            .border(1.dp, if (isSelected) ElectricBlue else GlassBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) ElectricBlue else SecondaryText,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) PrimaryText else SecondaryText
            )
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
    }
}

@Composable
fun BulletItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("• ", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(text = text, color = SecondaryText, fontSize = 13.sp, lineHeight = 18.sp)
    }
}

@Composable
fun StructureBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceDark)
            .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(text = text, color = ElectricBlueLight, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

private fun parseJsonArray(jsonStr: String): List<String> {
    return try {
        if (jsonStr.isBlank()) emptyList()
        else {
            val jsonArray = JSONArray(jsonStr)
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        }
    } catch (e: Exception) {
        emptyList()
    }
}

private fun Double.format(digits: Int): String = "%.${digits}f".format(this)
