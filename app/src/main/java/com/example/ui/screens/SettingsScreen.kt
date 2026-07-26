package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Language
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
import androidx.compose.ui.graphics.Color
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

@Composable
fun SettingsScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val profile = viewModel.userProfile.collectAsState().value

    var selectedAccent by remember(profile) { mutableStateOf(profile?.accentColorHex ?: "#3B82F6") }
    var selectedLanguage by remember(profile) { mutableStateOf(profile?.selectedLanguage ?: "English") }
    var selectedModel by remember(profile) { mutableStateOf(profile?.selectedAiModel ?: "gemini-3.5-flash") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo("HOME") },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CardDark)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryText)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Preferences & Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Text(
                            text = "App Theme, Language & AI Configuration",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Accent Color Picker
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ColorLens, contentDescription = null, tint = ElectricBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Primary Accent Color", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ColorSwatchPill("#3B82F6", Color(0xFF3B82F6), "Electric Blue", selectedAccent == "#3B82F6") {
                                selectedAccent = "#3B82F6"
                                viewModel.updateSettings(selectedAccent, selectedLanguage, selectedModel)
                            }
                            ColorSwatchPill("#10B981", Color(0xFF10B981), "Emerald", selectedAccent == "#10B981") {
                                selectedAccent = "#10B981"
                                viewModel.updateSettings(selectedAccent, selectedLanguage, selectedModel)
                            }
                            ColorSwatchPill("#8B5CF6", Color(0xFF8B5CF6), "Cyber Purple", selectedAccent == "#8B5CF6") {
                                selectedAccent = "#8B5CF6"
                                viewModel.updateSettings(selectedAccent, selectedLanguage, selectedModel)
                            }
                            ColorSwatchPill("#F59E0B", Color(0xFFF59E0B), "Amber", selectedAccent == "#F59E0B") {
                                selectedAccent = "#F59E0B"
                                viewModel.updateSettings(selectedAccent, selectedLanguage, selectedModel)
                            }
                        }
                    }
                }
            }

            // Language Selector
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = ElectricBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("AI Explanation Language", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val langs = listOf("English", "Tamil", "Tanglish")
                            langs.forEach { lang ->
                                val isSel = selectedLanguage == lang
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSel) ElectricBlue else BgDark)
                                        .clickable {
                                            selectedLanguage = lang
                                            viewModel.updateSettings(selectedAccent, selectedLanguage, selectedModel)
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = lang,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = if (isSel) PrimaryText else SecondaryText,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Gemini Model Selector
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricBlue)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Gemini AI Engine Model", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        val models = listOf(
                            "gemini-3.5-flash" to "Gemini 3.5 Flash (Ultra Fast)",
                            "gemini-3.1-pro-preview" to "Gemini 3.1 Pro (Deep Complex Reasoning)"
                        )

                        models.forEach { (key, label) ->
                            val isSel = selectedModel == key
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) ElectricBlue.copy(alpha = 0.2f) else BgDark)
                                    .border(1.dp, if (isSel) ElectricBlue else GlassBorder, RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedModel = key
                                        viewModel.updateSettings(selectedAccent, selectedLanguage, selectedModel)
                                    }
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSel) PrimaryText else SecondaryText,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }

            // About StudyMate AI Branding Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.dp, ElectricBlue, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_icon_logo_1785050242649),
                                contentDescription = "StudyMate AI Logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text("StudyMate AI v1.0", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                            Text("LEARN • FOCUS • ACHIEVE", style = MaterialTheme.typography.bodySmall, color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Production Ready • Built with Gemini & Firebase", style = MaterialTheme.typography.bodySmall, color = SecondaryText, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorSwatchPill(hex: String, color: Color, label: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(color)
                .border(if (isSelected) 3.dp else 0.dp, PrimaryText, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelMedium, fontSize = 10.sp, color = SecondaryText)
    }
}
