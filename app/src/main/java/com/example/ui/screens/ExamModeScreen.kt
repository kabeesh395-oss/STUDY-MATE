package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
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
import com.example.ui.theme.CardBorderDark
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

@Composable
fun ExamModeScreen(
    viewModel: StudyViewModel,
    modifier: Modifier = Modifier
) {
    var selectedMarkCategory by remember { mutableStateOf("13 Marks") }

    val questions = listOf(
        ExamQuestionItem(
            category = "13 Marks",
            title = "Q1. Explain A* Search Algorithm with a neat diagram and state space graph. Prove its admissibility.",
            repeatCount = 8,
            modelAnswer = "1. Introduction: A* is an informed search algorithm combining path cost g(n) and heuristic estimate h(n).\n2. Block Diagram: Draw Search Tree with Open & Closed sets.\n3. Formula: f(n) = g(n) + h(n).\n4. Admissibility Proof: h(n) <= h*(n) guarantees optimal path.",
            evaluationTip = "Award 3 marks for labeled tree diagram, 4 marks for f(n) derivation step, 3 marks for admissibility condition, 3 marks for example calculation."
        ),
        ExamQuestionItem(
            category = "13 Marks",
            title = "Q2. Derive the backpropagation weight update equations for a 2-layer Feedforward Neural Network.",
            repeatCount = 6,
            modelAnswer = "1. Loss function: E = 1/2 sum (y - t)^2.\n2. Chain Rule: dE/dw = (dE/da) * (da/dz) * (dz/dw).\n3. Gradient Descent Step: w_new = w_old - alpha * dE/dw.",
            evaluationTip = "Draw loss surface and clean chain rule equations for maximum university score."
        ),
        ExamQuestionItem(
            category = "2 Marks",
            title = "Q1. What is an admissible heuristic? State its mathematical condition.",
            repeatCount = 12,
            modelAnswer = "An admissible heuristic never overestimates the actual cost to reach the goal state. Condition: h(n) <= h*(n) for all nodes n.",
            evaluationTip = "Always write the mathematical inequality along with the 1-sentence definition."
        ),
        ExamQuestionItem(
            category = "16 Marks",
            title = "Q1. Comprehensive Case Study: Design an Agile DevOps CI/CD Pipeline for a Microservices Architecture.",
            repeatCount = 4,
            modelAnswer = "1. Architecture Overview.\n2. Containerization with Docker & Kubernetes.\n3. Automated Testing Stages.\n4. Blue-Green Deployment Strategy.",
            evaluationTip = "Include stage-wise architectural flowcharts for full 16-mark allocation."
        )
    ).filter { it.category == selectedMarkCategory }

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
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
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

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "University Exam Mode",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryText
                        )
                        Text(
                            text = "Question Bank & Model Answer Format",
                            style = MaterialTheme.typography.bodyMedium,
                            color = SecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.addXpPoints(20)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.FileDownload, contentDescription = "PDF", tint = ElectricBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("PDF Bank", color = PrimaryText, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Marks Selector Row
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("2 Marks", "5 Marks", "10 Marks", "13 Marks", "16 Marks")) { category ->
                    val isSelected = selectedMarkCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) ElectricBlue else CardDark)
                            .border(1.dp, if (isSelected) ElectricBlue else GlassBorder, RoundedCornerShape(14.dp))
                            .clickable { selectedMarkCategory = category }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) PrimaryText else SecondaryText,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Questions List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(questions) { q ->
                    ExamQuestionCard(item = q)
                }
            }
        }
    }
}

data class ExamQuestionItem(
    val category: String,
    val title: String,
    val repeatCount: Int,
    val modelAnswer: String,
    val evaluationTip: String
)

@Composable
fun ExamQuestionCard(item: ExamQuestionItem) {
    var expanded by remember { mutableStateOf(true) }

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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(ElectricBlue.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🔥 Repeated ${item.repeatCount}x in University Exams",
                        style = MaterialTheme.typography.labelMedium,
                        color = ElectricBlueLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = item.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = CyberPurple,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryText,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Model Answer Block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceDark)
                    .border(1.dp, CardBorderDark, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "📖 University Model Answer Format",
                        style = MaterialTheme.typography.labelMedium,
                        color = StatusSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.modelAnswer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryText,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "💡 Evaluation Tip: ${item.evaluationTip}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
