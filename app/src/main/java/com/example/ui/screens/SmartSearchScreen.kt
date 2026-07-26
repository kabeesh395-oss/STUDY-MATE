package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun SmartSearchScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val notes by viewModel.notes.collectAsState()

    val filteredSubjects = subjects.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.code.contains(searchQuery, ignoreCase = true)
    }

    val filteredNotes = notes.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.summary50Words.contains(searchQuery, ignoreCase = true)
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
            // Top Search Bar
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

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search subjects, formulas, definitions...", color = SecondaryText, fontSize = 13.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("global_search_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ElectricBlue,
                        unfocusedBorderColor = GlassBorder,
                        focusedContainerColor = CardDark,
                        unfocusedContainerColor = CardDark
                    ),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = ElectricBlue) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = SecondaryText)
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (searchQuery.isBlank()) {
                    item {
                        Text(
                            text = "Type keywords like 'A*', 'Neural', 'Software', 'Python' to instant search.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    item {
                        Text(
                            text = "Found ${filteredSubjects.size} Subjects & ${filteredNotes.size} Notes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ElectricBlueLight
                        )
                    }

                    items(filteredSubjects) { sub ->
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = CardDark,
                            borderColor = GlassBorder,
                            onClick = {
                                viewModel.selectSubject(sub)
                                viewModel.navigateTo("SUBJECT_DETAIL")
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Book, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = sub.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                                    Text(text = "${sub.code} • ${sub.semester}", style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    items(filteredNotes) { note ->
                        GlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = CardDark,
                            borderColor = GlassBorder,
                            onClick = {
                                viewModel.selectNote(note)
                                viewModel.navigateTo("SUBJECT_DETAIL")
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = ElectricBlueLight, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = note.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText)
                                    Text(text = note.summary50Words.take(80) + "...", style = MaterialTheme.typography.bodyMedium, color = SecondaryText, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
