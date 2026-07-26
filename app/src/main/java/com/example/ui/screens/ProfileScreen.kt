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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FirebaseAuthCard
import com.example.ui.components.GlassCard
import com.example.ui.theme.BgDark
import com.example.ui.theme.CardDark
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricBlueLight
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.PrimaryText
import com.example.ui.theme.SecondaryText
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun ProfileScreen(
    viewModel: StudyViewModel,
    authViewModel: AuthViewModel? = null,
    modifier: Modifier = Modifier
) {
    val profile = viewModel.userProfile.collectAsState().value
    var isEditing by remember { mutableStateOf(false) }

    val authUser = authViewModel?.currentUser?.collectAsState()?.value
    var name by remember(profile, authUser) { mutableStateOf(authUser?.name ?: profile?.name ?: "Kabeesh S") }
    var college by remember(profile, authUser) { mutableStateOf(authUser?.college ?: profile?.college ?: "College of Engineering, Guindy") }
    var dept by remember(profile, authUser) { mutableStateOf(authUser?.department ?: profile?.department ?: "Computer Science & Engineering") }
    var sem by remember(profile, authUser) { mutableStateOf(authUser?.semester ?: profile?.semester ?: "Semester 6") }

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
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
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Student Profile",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateTo("SETTINGS") },
                        modifier = Modifier.testTag("go_settings_btn")
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = ElectricBlue)
                    }
                }
            }

            // Firebase Authentication & Session Card
            if (authViewModel != null) {
                item {
                    FirebaseAuthCard(
                        authViewModel = authViewModel,
                        onNavigateLogin = { viewModel.navigateTo("LOGIN") },
                        onNavigateSignUp = { viewModel.navigateTo("SIGNUP") }
                    )
                }
            }

            // Avatar & Name Card
            item {
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardDark,
                    borderColor = GlassBorder
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(2.dp, ElectricBlue, CircleShape)
                                .background(Brush.linearGradient(listOf(ElectricBlue, CyberPurple))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile Pic",
                                tint = PrimaryText,
                                modifier = Modifier.size(50.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = name,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryText,
                            fontSize = 22.sp
                        )

                        Text(
                            text = "$dept • $sem",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ElectricBlueLight,
                            fontSize = 13.sp
                        )

                        Text(
                            text = college,
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ProfileStatPill("XP Points", "${profile?.xpPoints ?: 1250} XP", Icons.Default.EmojiEvents)
                            ProfileStatPill("Coins Balance", "${profile?.coins ?: 250}", Icons.Default.MonetizationOn)
                            ProfileStatPill("Study Hours", "28.5 Hrs", Icons.Default.School)
                        }
                    }
                }
            }

            // Profile Details & Edit
            item {
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Academic Details",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryText
                            )

                            IconButton(onClick = { isEditing = !isEditing }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = ElectricBlue)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isEditing) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name", color = SecondaryText) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = college,
                                onValueChange = { college = it },
                                label = { Text("College", color = SecondaryText) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = dept,
                                onValueChange = { dept = it },
                                label = { Text("Department", color = SecondaryText) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    viewModel.updateProfileInfo(name, college, dept, sem)
                                    isEditing = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Save Profile", color = PrimaryText)
                            }
                        } else {
                            DetailRow("Full Name", name)
                            DetailRow("College Name", college)
                            DetailRow("Department", dept)
                            DetailRow("Current Semester", sem)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStatPill(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, tint = ElectricBlue, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = PrimaryText, fontSize = 14.sp)
        Text(text = label, style = MaterialTheme.typography.labelMedium, fontSize = 10.sp, color = SecondaryText)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = SecondaryText, fontSize = 11.sp)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = PrimaryText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
