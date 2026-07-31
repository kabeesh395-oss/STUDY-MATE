package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import com.example.ui.components.BrandedLoadingIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entities.QuestionPaperEntity
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.MutedText
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.StudyViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionPaperBankScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allPapers by viewModel.allQuestionPapers.collectAsState()
    val currentFolderFilter by viewModel.paperFolderFilter.collectAsState()
    val currentSubjectFilter by viewModel.paperSubjectFilter.collectAsState()
    val currentYearFilter by viewModel.paperYearFilter.collectAsState()
    val searchQuery by viewModel.paperSearchQuery.collectAsState()
    val selectedPaper by viewModel.selectedPaper.collectAsState()
    val isProcessingUpload by viewModel.isProcessingPaperUpload.collectAsState()
    val uploadProgressText by viewModel.paperUploadProgressText.collectAsState()

    var showUploadDialog by remember { mutableStateOf(false) }
    var paperToRename by remember { mutableStateOf<QuestionPaperEntity?>(null) }
    var renameInputText by remember { mutableStateOf("") }
    var paperToDelete by remember { mutableStateOf<QuestionPaperEntity?>(null) }

    // Unique folders list
    val folderCategories = listOf(
        "ALL",
        "IA 1",
        "IA 2",
        "IA 3",
        "Model Exam",
        "Semester Exam",
        "University Previous Year Papers"
    )

    // Filter logic
    val filteredPapers = allPapers.filter { paper ->
        val matchesFolder = (currentFolderFilter == "ALL") || paper.examType.equals(currentFolderFilter, ignoreCase = true)
        val matchesSubject = (currentSubjectFilter == "ALL") || paper.subject.equals(currentSubjectFilter, ignoreCase = true)
        val matchesYear = (currentYearFilter == "ALL") || paper.academicYear.equals(currentYearFilter, ignoreCase = true)
        val matchesQuery = searchQuery.isBlank() ||
                paper.fileName.contains(searchQuery, ignoreCase = true) ||
                paper.subject.contains(searchQuery, ignoreCase = true) ||
                paper.department.contains(searchQuery, ignoreCase = true) ||
                paper.extractedQuestionsJson.contains(searchQuery, ignoreCase = true)

        matchesFolder && matchesSubject && matchesYear && matchesQuery
    }

    // Unique subjects list for filter dropdown
    val availableSubjects = remember(allPapers) {
        listOf("ALL") + allPapers.map { it.subject }.distinct()
    }

    // Unique academic years list for filter dropdown
    val availableYears = remember(allPapers) {
        listOf("ALL") + allPapers.map { it.academicYear }.distinct()
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
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo("HOME") },
                        modifier = Modifier.testTag("paper_bank_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PrimaryText
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(BgDark)
                            .border(1.dp, GlassBorder, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_lightning_s_logo),
                            contentDescription = "Logo",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Question Paper Bank",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Text(
                            text = "${filteredPapers.size} papers available • Gemini AI Analyzed",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryText,
                            fontSize = 11.sp
                        )
                    }
                }

                Button(
                    onClick = { showUploadDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("upload_paper_button")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Upload", tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Upload Paper", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setPaperSearchQuery(it) },
                placeholder = { Text("Search by subject, year, or question keyword...", color = MutedText, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = ElectricBlue) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setPaperSearchQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = SecondaryText)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("paper_search_field"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricBlue,
                    unfocusedBorderColor = GlassBorder,
                    focusedContainerColor = CardDark,
                    unfocusedContainerColor = CardDark,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = PrimaryText
                ),
                singleLine = true
            )

            // Folders Tabs Row (IA 1, IA 2, IA 3, Model Exam, Semester Exam, University Previous Year Papers)
            ScrollableTabRow(
                selectedTabIndex = folderCategories.indexOf(currentFolderFilter).coerceAtLeast(0),
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                contentColor = ElectricBlue,
                indicator = { tabPositions ->
                    val index = folderCategories.indexOf(currentFolderFilter).coerceAtLeast(0)
                    if (index < tabPositions.size) {
                        TabRowDefaults.PrimaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                            color = ElectricBlue,
                            height = 3.dp
                        )
                    }
                },
                divider = {}
            ) {
                folderCategories.forEach { folder ->
                    val isSelected = currentFolderFilter == folder
                    val count = if (folder == "ALL") allPapers.size else allPapers.count { it.examType.equals(folder, ignoreCase = true) }

                    Tab(
                        selected = isSelected,
                        onClick = { viewModel.setPaperFolderFilter(folder) },
                        modifier = Modifier.testTag("folder_tab_$folder")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = if (isSelected) ElectricBlue else SecondaryText,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = folder,
                                color = if (isSelected) PrimaryText else SecondaryText,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (isSelected) ElectricBlue.copy(alpha = 0.2f) else CardDark)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "$count",
                                    color = if (isSelected) ElectricBlue else MutedText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subject & Year Dropdown Filters Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Subject Filter Dropdown
                var subjectDropdownExpanded by remember { mutableStateOf(false) }
                Box {
                    Button(
                        onClick = { subjectDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CardDark),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Subject: ${if (currentSubjectFilter == "ALL") "All Subjects" else currentSubjectFilter}",
                            color = PrimaryText,
                            fontSize = 11.sp
                        )
                    }

                    DropdownMenu(
                        expanded = subjectDropdownExpanded,
                        onDismissRequest = { subjectDropdownExpanded = false },
                        modifier = Modifier.background(CardDark)
                    ) {
                        availableSubjects.forEach { sub ->
                            DropdownMenuItem(
                                text = { Text(sub, color = PrimaryText, fontSize = 12.sp) },
                                onClick = {
                                    viewModel.setPaperSubjectFilter(sub)
                                    subjectDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Year Filter Dropdown
                var yearDropdownExpanded by remember { mutableStateOf(false) }
                Box {
                    Button(
                        onClick = { yearDropdownExpanded = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CardDark),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.border(1.dp, GlassBorder, RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = "Year: ${if (currentYearFilter == "ALL") "All Years" else currentYearFilter}",
                            color = PrimaryText,
                            fontSize = 11.sp
                        )
                    }

                    DropdownMenu(
                        expanded = yearDropdownExpanded,
                        onDismissRequest = { yearDropdownExpanded = false },
                        modifier = Modifier.background(CardDark)
                    ) {
                        availableYears.forEach { yr ->
                            DropdownMenuItem(
                                text = { Text(yr, color = PrimaryText, fontSize = 12.sp) },
                                onClick = {
                                    viewModel.setPaperYearFilter(yr)
                                    yearDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Processing Banner if uploading
            AnimatedVisibility(visible = isProcessingUpload) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    backgroundColor = CardDark,
                    borderColor = ElectricBlue
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BrandedLoadingIndicator(
                            size = 32.dp,
                            strokeWidth = 2.5.dp,
                            logoSize = 16.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Gemini AI Analyzing Paper...", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(uploadProgressText, color = SecondaryText, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Paper Cards List
            if (filteredPapers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = MutedText,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Question Papers Found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap 'Upload Paper' to add IA, Model, or Semester papers for instant Gemini AI question extraction & analysis.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredPapers, key = { it.id }) { paper ->
                        SwipeableQuestionPaperCard(
                            paper = paper,
                            onOpen = { viewModel.setSelectedPaper(paper) },
                            onBookmarkToggle = { viewModel.toggleBookmarkPaper(paper) },
                            onDownload = {
                                viewModel.downloadPaperForOffline(paper)
                                Toast.makeText(context, "Paper cached offline for instant access!", Toast.LENGTH_SHORT).show()
                            },
                            onRenameClick = {
                                paperToRename = paper
                                renameInputText = paper.fileName.substringBeforeLast(".")
                            },
                            onDeleteClick = { paperToDelete = paper },
                            onShareClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, paper.fileName)
                                    putExtra(Intent.EXTRA_TEXT, "Check out this ${paper.examType} question paper for ${paper.subject} on StudyMate AI!\nFile: ${paper.fileName}")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Question Paper"))
                            }
                        )
                    }
                }
            }
        }

        // Full Detailed Paper Viewer Modal Dialog
        selectedPaper?.let { paper ->
            PaperDetailViewerModal(
                paper = paper,
                onDismiss = { viewModel.setSelectedPaper(null) },
                onToggleBookmark = { viewModel.toggleBookmarkPaper(paper) },
                onDownload = {
                    viewModel.downloadPaperForOffline(paper)
                    Toast.makeText(context, "Saved offline to device storage!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Upload Dialog
        if (showUploadDialog) {
            UploadPaperModal(
                availableSubjects = availableSubjects.filter { it != "ALL" },
                onDismiss = { showUploadDialog = false },
                onUploadConfirmed = { fileName, subject, examType, academicYear, department, semester, fileType ->
                    showUploadDialog = false
                    viewModel.uploadQuestionPaper(
                        fileName = fileName,
                        subject = subject,
                        examType = examType,
                        academicYear = academicYear,
                        department = department,
                        semester = semester,
                        fileType = fileType
                    )
                }
            )
        }

        // Rename Dialog
        paperToRename?.let { paper ->
            AlertDialog(
                onDismissRequest = { paperToRename = null },
                title = { Text("Rename Question Paper", color = PrimaryText, fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = renameInputText,
                        onValueChange = { renameInputText = it },
                        label = { Text("New File Name", color = SecondaryText) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = PrimaryText,
                            unfocusedTextColor = PrimaryText,
                            focusedBorderColor = ElectricBlue
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (renameInputText.isNotBlank()) {
                                viewModel.renameQuestionPaper(paper, renameInputText.trim())
                                Toast.makeText(context, "Paper renamed successfully", Toast.LENGTH_SHORT).show()
                            }
                            paperToRename = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                    ) {
                        Text("Rename", color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { paperToRename = null }) {
                        Text("Cancel", color = SecondaryText)
                    }
                },
                containerColor = CardDark
            )
        }

        // Delete Confirmation Dialog
        paperToDelete?.let { paper ->
            AlertDialog(
                onDismissRequest = { paperToDelete = null },
                title = { Text("Delete Question Paper?", color = PrimaryText, fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to delete '${paper.fileName}'? This action cannot be undone.", color = SecondaryText) },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteQuestionPaper(paper.id)
                            paperToDelete = null
                            Toast.makeText(context, "Question paper deleted", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { paperToDelete = null }) {
                        Text("Cancel", color = SecondaryText)
                    }
                },
                containerColor = CardDark
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableQuestionPaperCard(
    paper: QuestionPaperEntity,
    onOpen: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onDownload: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onDeleteClick()
                    false
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onBookmarkToggle()
                    false
                }
                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFEF4444).copy(alpha = 0.85f)
                    SwipeToDismissBoxValue.StartToEnd -> ElectricBlue.copy(alpha = 0.85f)
                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                },
                animationSpec = tween(300),
                label = "swipeBgColor"
            )

            val direction = dismissState.dismissDirection

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = when (direction) {
                    SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                    SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
                    else -> Alignment.Center
                }
            ) {
                if (direction == SwipeToDismissBoxValue.EndToStart) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Delete Paper",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else if (direction == SwipeToDismissBoxValue.StartToEnd) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (paper.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (paper.isBookmarked) "Bookmarked" else "Bookmark",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        content = {
            QuestionPaperCard(
                paper = paper,
                onOpen = onOpen,
                onBookmarkToggle = onBookmarkToggle,
                onDownload = onDownload,
                onRenameClick = onRenameClick,
                onDeleteClick = onDeleteClick,
                onShareClick = onShareClick
            )
        }
    )
}

@Composable
fun QuestionPaperCard(
    paper: QuestionPaperEntity,
    onOpen: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onDownload: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShareClick: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    // Color theme badge for Exam Types
    val examBadgeColor = when {
        paper.examType.contains("IA", ignoreCase = true) -> ElectricBlue
        paper.examType.contains("Model", ignoreCase = true) -> CyberPurple
        paper.examType.contains("Semester", ignoreCase = true) -> NeonCyan
        else -> Color(0xFFF59E0B) // Golden orange for University
    }

    val formattedDate = remember(paper.uploadDate) {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        sdf.format(Date(paper.uploadDate))
    }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("question_paper_card_${paper.id}"),
        backgroundColor = CardDark,
        borderColor = GlassBorder
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(examBadgeColor.copy(alpha = 0.15f))
                            .border(1.dp, examBadgeColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (paper.fileType.uppercase()) {
                                "PDF" -> Icons.Default.Description
                                "IMAGE" -> Icons.Default.Description
                                else -> Icons.Default.Folder
                            },
                            contentDescription = paper.fileType,
                            tint = examBadgeColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = paper.fileName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = paper.subject,
                                style = MaterialTheme.typography.bodySmall,
                                color = SecondaryText,
                                fontWeight = FontWeight.Medium
                            )
                            Text(" • ", color = MutedText, fontSize = 10.sp)
                            Text(
                                text = paper.academicYear,
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedText
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBookmarkToggle) {
                        Icon(
                            imageVector = if (paper.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (paper.isBookmarked) ElectricBlue else MutedText
                        )
                    }

                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = SecondaryText)
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(CardDark)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Open & AI Analyze", color = PrimaryText, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.OpenInNew, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onOpen()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Download Offline", color = PrimaryText, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Download, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onDownload()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Share Paper", color = PrimaryText, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Share, contentDescription = null, tint = CyberPurple, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onShareClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Rename", color = PrimaryText, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = PrimaryText, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onRenameClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete", color = Color.Red, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onDeleteClick()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metadata Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(examBadgeColor.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = paper.examType,
                            color = examBadgeColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    if (paper.semester.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricBlue.copy(alpha = 0.1f))
                                .border(1.dp, ElectricBlue.copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Sem ${paper.semester}",
                                color = ElectricBlue,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CardDark)
                            .border(1.dp, GlassBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = paper.fileType.uppercase(),
                            color = SecondaryText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    if (paper.isDownloaded) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Offline Ready",
                                tint = NeonCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Offline", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Text(
                    text = "${paper.fileSizeFormatted} • $formattedDate",
                    color = MutedText,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun PaperDetailViewerModal(
    paper: QuestionPaperEntity,
    onDismiss: () -> Unit,
    onToggleBookmark: () -> Unit,
    onDownload: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Extracted Questions", "Repeated Questions", "AI Important", "Model Answers")

    // Parse JSON data
    val questionsList = remember(paper.extractedQuestionsJson) {
        try {
            val arr = JSONArray(paper.extractedQuestionsJson)
            List(arr.length()) { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    val markCategories = remember(paper.markCategoriesJson) {
        try {
            val json = JSONObject(paper.markCategoriesJson)
            mapOf(
                "2" to List(json.optJSONArray("2")?.length() ?: 0) { json.getJSONArray("2").getString(it) },
                "5" to List(json.optJSONArray("5")?.length() ?: 0) { json.getJSONArray("5").getString(it) },
                "10" to List(json.optJSONArray("10")?.length() ?: 0) { json.getJSONArray("10").getString(it) },
                "13" to List(json.optJSONArray("13")?.length() ?: 0) { json.getJSONArray("13").getString(it) },
                "16" to List(json.optJSONArray("16")?.length() ?: 0) { json.getJSONArray("16").getString(it) }
            )
        } catch (e: Exception) {
            emptyMap()
        }
    }

    val repeatedQuestions = remember(paper.repeatedQuestionsJson) {
        try {
            val arr = JSONArray(paper.repeatedQuestionsJson)
            List(arr.length()) { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    val importantQuestions = remember(paper.importantQuestionsJson) {
        try {
            val arr = JSONArray(paper.importantQuestionsJson)
            List(arr.length()) { arr.getString(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    val generatedAnswers = remember(paper.generatedAnswersJson) {
        try {
            val json = JSONObject(paper.generatedAnswersJson)
            val map = mutableMapOf<String, String>()
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key] = json.getString(key)
            }
            map
        } catch (e: Exception) {
            emptyMap()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(520.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = paper.fileName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${paper.subject} • ${paper.examType} • ${paper.academicYear}",
                            style = MaterialTheme.typography.bodySmall,
                            color = ElectricBlue,
                            fontSize = 11.sp
                        )
                    }

                    Row {
                        IconButton(onClick = onToggleBookmark) {
                            Icon(
                                imageVector = if (paper.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (paper.isBookmarked) ElectricBlue else SecondaryText
                            )
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = SecondaryText)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tabs inside viewer
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = ElectricBlue,
                    edgePadding = 0.dp,
                    divider = {}
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }
                        ) {
                            Text(
                                text = title,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTab == index) ElectricBlue else SecondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tab Content
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    when (selectedTab) {
                        0 -> {
                            // Extracted Questions by Marks (2, 5, 10, 13, 16 Marks)
                            val markTypes = listOf("2", "5", "10", "13", "16")
                            markTypes.forEach { mark ->
                                val qs = markCategories[mark] ?: emptyList()
                                if (qs.isNotEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ElectricBlue.copy(alpha = 0.12f))
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "📌 $mark MARKS QUESTIONS",
                                                color = ElectricBlue,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    items(qs) { qText ->
                                        GlassCard(
                                            modifier = Modifier.fillMaxWidth(),
                                            backgroundColor = CardDark,
                                            borderColor = GlassBorder
                                        ) {
                                            Text(
                                                text = qText,
                                                color = PrimaryText,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Fallback if mark categories empty
                            if (markCategories.values.all { it.isEmpty() }) {
                                items(questionsList) { qText ->
                                    GlassCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        backgroundColor = CardDark,
                                        borderColor = GlassBorder
                                    ) {
                                        Text(
                                            text = qText,
                                            color = PrimaryText,
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(10.dp)
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            // Repeated Questions
                            if (repeatedQuestions.isEmpty()) {
                                item {
                                    Text("No repeated questions detected for this paper.", color = SecondaryText, fontSize = 12.sp)
                                }
                            } else {
                                items(repeatedQuestions) { repText ->
                                    GlassCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        backgroundColor = CardDark,
                                        borderColor = NeonCyan.copy(alpha = 0.5f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Psychology, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = repText,
                                                color = PrimaryText,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        2 -> {
                            // AI Important Questions
                            if (importantQuestions.isEmpty()) {
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    ) {
                                        BrandedLoadingIndicator(size = 28.dp, strokeWidth = 2.dp, logoSize = 14.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("AI important questions generating...", color = SecondaryText, fontSize = 12.sp)
                                    }
                                }
                            } else {
                                items(importantQuestions) { impText ->
                                    GlassCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        backgroundColor = CardDark,
                                        borderColor = CyberPurple
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberPurple, modifier = Modifier.size(20.dp))
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = impText,
                                                color = PrimaryText,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        3 -> {
                            // Model Answers
                            if (generatedAnswers.isEmpty()) {
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    ) {
                                        BrandedLoadingIndicator(size = 28.dp, strokeWidth = 2.dp, logoSize = 14.dp)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("Model answers generating from study materials...", color = SecondaryText, fontSize = 12.sp)
                                    }
                                }
                            } else {
                                items(generatedAnswers.entries.toList()) { entry ->
                                    GlassCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        backgroundColor = CardDark,
                                        borderColor = GlassBorder
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = entry.key,
                                                color = ElectricBlue,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = entry.value,
                                                color = PrimaryText,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDownload,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Download Offline", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = SecondaryText)
            }
        },
        containerColor = CardDark
    )
}

@Composable
fun UploadPaperModal(
    availableSubjects: List<String>,
    onDismiss: () -> Unit,
    onUploadConfirmed: (fileName: String, subject: String, examType: String, academicYear: String, department: String, semester: String, fileType: String) -> Unit
) {
    var fileName by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf(availableSubjects.firstOrNull() ?: "Artificial Intelligence") }
    var selectedExamType by remember { mutableStateOf("IA 1") }
    var selectedYear by remember { mutableStateOf("2024-2025") }
    var department by remember { mutableStateOf("Computer Science & Engineering") }
    var semester by remember { mutableStateOf("Semester 5") }
    var selectedFileType by remember { mutableStateOf("PDF") }

    val examTypes = listOf("IA 1", "IA 2", "IA 3", "Model Exam", "Semester Exam", "University Previous Year Papers")
    val fileTypes = listOf("PDF", "IMAGE", "DOCX")
    val yearOptions = listOf("2024-2025", "2023-2024", "2022-2023", "2021-2022")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.UploadFile, contentDescription = null, tint = ElectricBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Upload Question Paper", color = PrimaryText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Paper Title / File Name", color = SecondaryText, fontSize = 12.sp) },
                    placeholder = { Text("e.g. CS8691 AI IA1 2024", color = MutedText, fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = PrimaryText,
                        unfocusedTextColor = PrimaryText,
                        focusedBorderColor = ElectricBlue
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Exam Type Selector Chips
                Text("Select Folder / Exam Type:", color = PrimaryText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(examTypes) { type ->
                        FilterChip(
                            selected = selectedExamType == type,
                            onClick = { selectedExamType = type },
                            label = { Text(type, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricBlue,
                                selectedLabelColor = Color.Black,
                                containerColor = CardDark,
                                labelColor = SecondaryText
                            )
                        )
                    }
                }

                // File Type Chips (PDF, Image, DOCX)
                Text("File Format:", color = PrimaryText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    fileTypes.forEach { ft ->
                        FilterChip(
                            selected = selectedFileType == ft,
                            onClick = { selectedFileType = ft },
                            label = { Text(ft, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonCyan,
                                selectedLabelColor = Color.Black,
                                containerColor = CardDark,
                                labelColor = SecondaryText
                            )
                        )
                    }
                }

                // Subject Name Selection
                OutlinedTextField(
                    value = selectedSubject,
                    onValueChange = { selectedSubject = it },
                    label = { Text("Subject Name", color = SecondaryText, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = PrimaryText,
                        unfocusedTextColor = PrimaryText,
                        focusedBorderColor = ElectricBlue
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Academic Year & Semester Row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = selectedYear,
                        onValueChange = { selectedYear = it },
                        label = { Text("Academic Year", color = SecondaryText, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = semester,
                        onValueChange = { semester = it },
                        label = { Text("Semester", color = SecondaryText, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = PrimaryText, unfocusedTextColor = PrimaryText),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val title = if (fileName.isBlank()) "$selectedSubject $selectedExamType $selectedYear" else fileName.trim()
                    onUploadConfirmed(
                        title,
                        selectedSubject.trim(),
                        selectedExamType,
                        selectedYear,
                        department,
                        semester,
                        selectedFileType
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Upload & AI Analyze", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SecondaryText)
            }
        },
        containerColor = CardDark
    )
}
