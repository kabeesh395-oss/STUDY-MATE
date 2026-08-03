package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.components.BottomNavBar
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.CareerHubScreen
import com.example.ui.screens.DashboardAnalyticsScreen
import com.example.ui.screens.ExamModeScreen
import com.example.ui.screens.FlashcardsScreen
import com.example.ui.screens.GamificationScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuestionPaperBankScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.RevisionPlannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.SmartSearchScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.SubjectDetailScreen
import com.example.ui.screens.UploadNotesScreen
import com.example.ui.theme.BgDark
import com.example.ui.theme.StudyMateTheme
import com.example.ui.viewmodel.StudyViewModel

import com.example.ui.screens.LoginScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.viewmodel.AuthViewModel

import com.example.utils.StudyNotificationManager
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val viewModel: StudyViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ensure Firebase App is initialized
        try {
            if (com.google.firebase.FirebaseApp.getApps(this).isEmpty()) {
                com.google.firebase.FirebaseApp.initializeApp(this)
            }
        } catch (e: Exception) {
            android.util.Log.e("MainActivity", "Firebase init warning: ${e.message}")
        }

        // Initialize StudyMate AI Notification Channels and Manager
        val notificationManager = StudyNotificationManager.getInstance(this)

        // Request notification permissions for Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        setContent {
            val themeMode by viewModel.themeMode.collectAsState()

            StudyMateTheme(themeMode = themeMode) {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val bg = androidx.compose.material3.MaterialTheme.colorScheme.background

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bg),
                    bottomBar = {
                        // Show bottom nav on main navigation screens
                        if (currentScreen in listOf("HOME", "QUESTION_BANK", "CAREER_HUB", "AI_CHAT", "EXAM_MODE", "QUIZ", "FLASHCARDS", "PROFILE")) {
                            BottomNavBar(
                                currentScreen = currentScreen,
                                onNavigate = { viewModel.navigateTo(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(bg)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                (fadeIn(animationSpec = tween(280, easing = FastOutSlowInEasing)) +
                                 scaleIn(initialScale = 0.96f, animationSpec = tween(280, easing = FastOutSlowInEasing)) +
                                 slideInHorizontally(initialOffsetX = { it / 8 }, animationSpec = tween(280, easing = FastOutSlowInEasing)))
                                    .togetherWith(
                                        fadeOut(animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                                        scaleOut(targetScale = 0.98f, animationSpec = tween(220, easing = FastOutSlowInEasing)) +
                                        slideOutHorizontally(targetOffsetX = { -it / 8 }, animationSpec = tween(220, easing = FastOutSlowInEasing))
                                    )
                            },
                            label = "screenTransition"
                        ) { screen ->
                            when (screen) {
                                "SPLASH" -> SplashScreen(onSplashComplete = { viewModel.navigateTo("HOME") })
                                "HOME" -> HomeScreen(viewModel = viewModel)
                                "QUESTION_BANK" -> QuestionPaperBankScreen(viewModel = viewModel)
                                "CAREER_HUB" -> CareerHubScreen(viewModel = viewModel, authViewModel = authViewModel)
                                "SUBJECT_DETAIL" -> SubjectDetailScreen(viewModel = viewModel)
                                "UPLOAD" -> UploadNotesScreen(viewModel = viewModel)
                                "AI_CHAT" -> AiChatScreen(viewModel = viewModel)
                                "EXAM_MODE" -> ExamModeScreen(viewModel = viewModel)
                                "QUIZ" -> QuizScreen(viewModel = viewModel)
                                "FLASHCARDS" -> FlashcardsScreen(viewModel = viewModel)
                                "REVISION" -> RevisionPlannerScreen(viewModel = viewModel)
                                "ANALYTICS" -> DashboardAnalyticsScreen(viewModel = viewModel)
                                "GAMIFICATION" -> GamificationScreen(viewModel = viewModel)
                                "SEARCH" -> SmartSearchScreen(viewModel = viewModel)
                                "PROFILE" -> ProfileScreen(viewModel = viewModel, authViewModel = authViewModel)
                                "SETTINGS" -> SettingsScreen(viewModel = viewModel)
                                "NOTIFICATIONS" -> com.example.ui.screens.NotificationsScreen(viewModel = viewModel)
                                "LOGIN" -> LoginScreen(authViewModel = authViewModel, studyViewModel = viewModel)
                                "SIGNUP" -> SignUpScreen(authViewModel = authViewModel, studyViewModel = viewModel)
                                else -> HomeScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
