package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.ChatMessageEntity
import com.example.ui.components.BenAvatar
import com.example.ui.components.BenMood
import com.example.ui.components.GlassCard
import com.example.ui.components.TypingIndicator
import com.example.ui.components.VoicePlayerBar
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.theme.SurfaceDark
import com.example.ui.viewmodel.StudyViewModel

/*
 * TODO: [GEMINI API INTEGRATION PLACEHOLDER FOR AI TUTOR SCREEN]
 * To connect live Gemini API:
 * 1. Store your GEMINI_API_KEY in the AI Studio Secrets Panel or .env file.
 * 2. Access BuildConfig.GEMINI_API_KEY in RetrofitClient.kt.
 * 3. Invoke RetrofitClient.geminiService.generateContent("gemini-3.5-flash", apiKey, request).
 */

@Composable
fun AiChatScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var inputText by remember { mutableStateOf("") }
    var useOnlyNotes by remember { mutableStateOf(false) }

    // Explanation Mode: BEGINNER, COLLEGE, EXAM, VIVA
    var selectedMode by remember { mutableStateOf("EXAM") }

    // Language: English, Tamil, Tanglish
    var selectedLanguage by remember { mutableStateOf("English") }

    // Output Generation Type
    var selectedFormat by remember { mutableStateOf<String?>(null) }

    // Attachments
    var attachedPdf by remember { mutableStateOf<String?>(null) }
    var attachedImage by remember { mutableStateOf<String?>(null) }

    val chatMessages by viewModel.chatMessages.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    val isPlayingVoice by viewModel.isPlayingVoice.collectAsState()
    val voiceText by viewModel.currentVoiceText.collectAsState()
    val voiceSpeed by viewModel.voiceSpeed.collectAsState()

    // Requirement 4: Quick Prompt Chips
    val quickPrompts = listOf(
        "Explain this",
        "Summarize",
        "Important Questions",
        "Quiz Me",
        "Flashcards",
        "Viva Questions",
        "Formula Sheet"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateTo("HOME") },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CardDark)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    BenAvatar(mood = if (isThinking) BenMood.THINKING else BenMood.HAPPY, size = 42.dp)

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Ben • AI Tutor",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "Personal AI Study Companion",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 11.sp,
                            color = ElectricBlueLight
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.clearChat() },
                    modifier = Modifier.testTag("clear_chat_btn")
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Chat", tint = SecondaryText)
                }
            }

            // 2. Control Bar (Modes, Language, Formats)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardDark)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row A: Notes Context Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (useOnlyNotes) "🔒 Rely strictly on uploaded study notes" else "🌐 Notes + AI Textbook Knowledge",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 12.sp,
                        color = if (useOnlyNotes) ElectricBlueLight else SecondaryText,
                        fontWeight = FontWeight.SemiBold
                    )

                    Switch(
                        checked = useOnlyNotes,
                        onCheckedChange = { useOnlyNotes = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PrimaryText,
                            checkedTrackColor = ElectricBlue,
                            uncheckedThumbColor = SecondaryText,
                            uncheckedTrackColor = SurfaceDark
                        ),
                        modifier = Modifier.testTag("notes_only_switch")
                    )
                }

                // Row B: Explanation Mode Pills (Beginner, College, Exam, Viva)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mode:", style = MaterialTheme.typography.labelMedium, color = SecondaryText, fontSize = 11.sp)

                    Spacer(modifier = Modifier.width(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item { ControlPill("🌱 Beginner", selectedMode == "BEGINNER") { selectedMode = "BEGINNER" } }
                        item { ControlPill("🏛️ College", selectedMode == "COLLEGE") { selectedMode = "COLLEGE" } }
                        item { ControlPill("📝 Exam", selectedMode == "EXAM") { selectedMode = "EXAM" } }
                        item { ControlPill("🎙️ Viva Voce", selectedMode == "VIVA") { selectedMode = "VIVA" } }
                    }
                }

                // Row C: Language Pills (English, Tamil, Tanglish)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = CyberPurple, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Language:", style = MaterialTheme.typography.labelMedium, color = SecondaryText, fontSize = 11.sp)

                    Spacer(modifier = Modifier.width(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item { ControlPill("🇬🇧 English", selectedLanguage == "English") { selectedLanguage = "English" } }
                        item { ControlPill("🇮🇳 தமிழ் (Tamil)", selectedLanguage == "Tamil") { selectedLanguage = "Tamil" } }
                        item { ControlPill("🗣️ Tanglish", selectedLanguage == "Tanglish") { selectedLanguage = "Tanglish" } }
                    }
                }

                // Row D: Specific AI Format Outputs
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val formats = listOf(
                        "2-Mark Qs" to "2-Mark Questions",
                        "5-Mark Qs" to "5-Mark Questions",
                        "13/16-Mark" to "13/16-Mark Answer",
                        "MCQs" to "Multiple Choice Questions",
                        "Flashcards" to "Flashcards",
                        "Mind Map" to "Mind Map",
                        "Flowchart" to "Flowchart"
                    )
                    items(formats) { (label, fmtKey) ->
                        val isSel = selectedFormat == fmtKey
                        ControlPill(
                            label = label,
                            isSelected = isSel,
                            activeColor = CyberPurple,
                            onClick = { selectedFormat = if (isSel) null else fmtKey }
                        )
                    }
                }
            }

            // Voice Player if active
            if (voiceText.isNotBlank()) {
                VoicePlayerBar(
                    text = voiceText,
                    isPlaying = isPlayingVoice,
                    speed = voiceSpeed,
                    onTogglePlay = { viewModel.toggleVoicePlayback(voiceText) },
                    onSpeedChange = { viewModel.setVoiceSpeed(it) }
                )
            }

            val listState = rememberLazyListState()

            LaunchedEffect(chatMessages.size, isThinking) {
                if (chatMessages.isNotEmpty()) {
                    listState.animateScrollToItem(chatMessages.size - 1)
                }
            }

            // 3. Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                if (chatMessages.isEmpty()) {
                    item {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            backgroundColor = SurfaceDark,
                            borderColor = GlassBorder
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                BenAvatar(mood = BenMood.HAPPY, size = 80.dp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Hello Kabeesh 👋",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryText
                                )
                                Text(
                                    text = "I'm Ben, your personal AI study assistant.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = ElectricBlueLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text("I can help you with:", style = MaterialTheme.typography.labelLarge, color = SecondaryText)
                                    Text("• Explain topics & concepts deeply", style = MaterialTheme.typography.bodySmall, color = PrimaryText)
                                    Text("• Summarize PDFs & uploaded notes", style = MaterialTheme.typography.bodySmall, color = PrimaryText)
                                    Text("• Create flashcards & generate MCQs", style = MaterialTheme.typography.bodySmall, color = PrimaryText)
                                    Text("• Generate 2, 5, 10, 13, 16 mark exam answers", style = MaterialTheme.typography.bodySmall, color = PrimaryText)
                                    Text("• Explain in Beginner, College, Exam modes", style = MaterialTheme.typography.bodySmall, color = PrimaryText)
                                    Text("• Multilingual: English, Tamil, Tanglish", style = MaterialTheme.typography.bodySmall, color = PrimaryText)
                                }
                            }
                        }
                    }
                }

                items(chatMessages) { msg ->
                    ChatMessageBubble(
                        message = msg,
                        onReadVoice = { viewModel.toggleVoicePlayback(msg.message) },
                        onCopyText = {
                            clipboardManager.setText(AnnotatedString(msg.message))
                            Toast.makeText(context, "Copied response to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        onShareText = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "StudyMate AI Response")
                                putExtra(Intent.EXTRA_TEXT, msg.message)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share AI Response"))
                        },
                        onSaveToNotes = {
                            viewModel.saveChatMessageToNotes(
                                title = "AI Note: " + msg.message.take(30).replace("\n", " "),
                                content = msg.message
                            )
                            Toast.makeText(context, "Saved to Study Notes!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                if (isThinking) {
                    item {
                        GlassCard(
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .padding(vertical = 6.dp),
                            backgroundColor = SurfaceDark,
                            borderColor = ElectricBlue
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TypingIndicator()
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "StudyMate AI is thinking...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = ElectricBlueLight,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Generating ${selectedFormat ?: "explanation"} in $selectedLanguage",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SecondaryText,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 4. Quick Prompt Chips Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickPrompts) { prompt ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(SurfaceDark)
                            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                            .clickable {
                                inputText = prompt
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = prompt,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 11.sp,
                            color = SecondaryText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // 5. Active Attachment Chips Bar (PDF & Image)
            if (attachedPdf != null || attachedImage != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    attachedPdf?.let { pdfName ->
                        AttachmentBadge(
                            icon = Icons.Default.PictureAsPdf,
                            label = pdfName,
                            onRemove = { attachedPdf = null }
                        )
                    }

                    attachedImage?.let { imgName ->
                        AttachmentBadge(
                            icon = Icons.Default.Image,
                            label = imgName,
                            onRemove = { attachedImage = null }
                        )
                    }
                }
            }

            // 6. Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceDark)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Attach PDF Button
                IconButton(
                    onClick = {
                        attachedPdf = "Unit3_DataStructures_Notes.pdf"
                        Toast.makeText(context, "Attached PDF for AI Summarization", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PictureAsPdf,
                        contentDescription = "Attach PDF",
                        tint = if (attachedPdf != null) ElectricBlue else SecondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Attach Image Button
                IconButton(
                    onClick = {
                        attachedImage = "CircuitDiagram_Scan.png"
                        Toast.makeText(context, "Attached Image for AI Vision Analysis", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Attach Image",
                        tint = if (attachedImage != null) ElectricBlue else SecondaryText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask anything to your AI tutor...", color = SecondaryText, fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = CardDark,
                        unfocusedContainerColor = CardDark
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            inputText = "Explain the A* Search algorithm for my Anna University exam."
                            Toast.makeText(context, "Voice input activated", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = SecondaryText)
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (inputText.isNotBlank() || attachedPdf != null || attachedImage != null) {
                            viewModel.sendChatMessage(
                                userPrompt = inputText,
                                useOnlyNotes = useOnlyNotes,
                                modeOverride = selectedMode,
                                language = selectedLanguage,
                                outputFormat = selectedFormat,
                                attachedPdfName = attachedPdf,
                                attachedImageName = attachedImage
                            )
                            inputText = ""
                            attachedPdf = null
                            attachedImage = null
                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ElectricBlue)
                        .size(46.dp)
                        .testTag("send_chat_btn")
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send", tint = PrimaryText)
                }
            }
        }
    }
}

@Composable
fun ControlPill(
    label: String,
    isSelected: Boolean,
    activeColor: Color = ElectricBlue,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.25f) else SurfaceDark)
            .border(1.dp, if (isSelected) activeColor else GlassBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontSize = 11.sp,
            color = if (isSelected) PrimaryText else SecondaryText,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun AttachmentBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(ElectricBlue.copy(alpha = 0.15f))
            .border(1.dp, ElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontSize = 11.sp, color = PrimaryText)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Remove",
                tint = SecondaryText,
                modifier = Modifier
                    .size(14.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

@Composable
fun ChatMessageBubble(
    message: ChatMessageEntity,
    onReadVoice: () -> Unit,
    onCopyText: () -> Unit,
    onShareText: () -> Unit,
    onSaveToNotes: () -> Unit
) {
    val isUser = message.sender == "USER"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        GlassCard(
            modifier = Modifier.fillMaxWidth(if (isUser) 0.8f else 0.92f),
            backgroundColor = if (isUser) ElectricBlue.copy(alpha = 0.2f) else CardDark,
            borderColor = if (isUser) ElectricBlue else GlassBorder,
            cornerRadius = 18.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Top Action Bar on AI Bubble
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "You" else "StudyMate AI",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isUser) ElectricBlueLight else CyberPurple,
                        fontSize = 11.sp
                    )

                    if (!isUser) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Read Aloud
                            IconButton(onClick = onReadVoice, modifier = Modifier.size(26.dp)) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Read Aloud", tint = SecondaryText, modifier = Modifier.size(15.dp))
                            }
                            // Copy Response
                            IconButton(onClick = onCopyText, modifier = Modifier.size(26.dp)) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Copy Response", tint = SecondaryText, modifier = Modifier.size(15.dp))
                            }
                            // Share Response
                            IconButton(onClick = onShareText, modifier = Modifier.size(26.dp)) {
                                Icon(Icons.Default.Share, contentDescription = "Share Response", tint = SecondaryText, modifier = Modifier.size(15.dp))
                            }
                            // Save to Notes
                            IconButton(onClick = onSaveToNotes, modifier = Modifier.size(26.dp)) {
                                Icon(Icons.Default.Bookmark, contentDescription = "Save to Notes", tint = ElectricBlue, modifier = Modifier.size(15.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (message.isCode) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(BgDark)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = message.message,
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace,
                            color = PrimaryText,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Text(
                        text = message.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = PrimaryText,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
